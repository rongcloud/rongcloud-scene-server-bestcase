package cn.rongcloud.mic.community.service.impl;


import cn.rongcloud.common.utils.IdentifierUtils;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.redis.RedisUtils;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.rest.RestResultCode;
import cn.rongcloud.mic.common.utils.CoverUtil;
import cn.rongcloud.mic.community.constant.CommunityConstant;
import cn.rongcloud.mic.community.dto.TGroupDTO;
import cn.rongcloud.mic.community.dto.TGroupSaveReq;
import cn.rongcloud.mic.community.dto.TGroupUpdateReq;
import cn.rongcloud.mic.community.entity.TChannel;
import cn.rongcloud.mic.community.entity.TCommunity;
import cn.rongcloud.mic.community.entity.TGroup;
import cn.rongcloud.mic.community.mapper.TGroupMapper;
import cn.rongcloud.mic.community.message.CommunityMessageUtil;
import cn.rongcloud.mic.community.service.TChannelService;
import cn.rongcloud.mic.community.service.TCommunityService;
import cn.rongcloud.mic.community.service.TGroupService;
import cn.rongcloud.mic.community.vo.TGroupVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TGroupServiceImpl implements TGroupService {

    @Resource
    private TGroupMapper TGroupMapper;

    @Autowired
    private TChannelService TChannelService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private CommunityMessageUtil communityMessageUtil;

    @Autowired
    private TCommunityService tCommunityService;

    @Override
    public RestResult<IPage<TGroupVO>> queryPage(TGroupDTO params) {
        Page<TGroup> pageParam = new Page<>();
        pageParam.setCurrent(params.getPageNum());
        pageParam.setSize(params.getPageSize());
        IPage pageResult = TGroupMapper.selectPage(pageParam, new QueryWrapper<TGroup>().lambda().eq(TGroup::getDelFlag,0));
        List<TGroup> records = pageResult.getRecords();
        List<TGroupVO> pageVOS = CoverUtil.sourceToTarget(records, TGroupVO.class);
        pageResult.setRecords(pageVOS);
        return RestResult.success(pageResult);
    }

    @Override
    public TGroup queryByUid(String uid) {
        if(StringUtils.isBlank(uid)){
            return null;
        }
        // 缓存拿
        String groupInfoKey = String.format(CommunityConstant.GROUP_INFO, uid);
        TGroup entity = (TGroup)redisUtils.get(groupInfoKey);
        if(entity!=null){
            return entity;
        }
        entity = TGroupMapper.selectOne(new QueryWrapper<TGroup>().lambda().eq(TGroup::getUid,uid).eq(TGroup::getDelFlag,0));
        if(entity!=null){
            redisUtils.set(groupInfoKey,entity,CommunityConstant.INFO_TIME);
        }
        return entity;
    }

    @Override
    public List<TGroup> queryByCommunityUid(String communityUid) {
        return TGroupMapper.selectList(new QueryWrapper<TGroup>().lambda().eq(TGroup::getCommunityUid,communityUid).eq(TGroup::getDelFlag,0));
    }

    @Override
    public RestResult<String> save(TGroupSaveReq params, JwtUser jwtUser) {
        if(StringUtils.isBlank(params.getCommunityUid())||StringUtils.isBlank(params.getName())){
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        TGroup group = queryByCommunityUidAndName(params.getCommunityUid(), params.getName());
        if(group!=null){
            return RestResult.generic(RestResultCode.ERR_GROUP_NAME);
        }
        String groupUid = IdentifierUtils.uuid();
        int sort = TGroupMapper.maxSort(params.getCommunityUid());
        TGroup entity = TGroup.builder().communityUid(params.getCommunityUid()).name(params.getName())
                .creator(jwtUser.getUserId()).sort(sort+1).uid(groupUid).build();
        TGroupMapper.insert(entity);
        communityMessageUtil.sendCommunityChangeMessage(jwtUser.getUserId(),params.getCommunityUid(),Lists.newArrayList());
        return RestResult.success(groupUid);
    }

    @Override
    public RestResult delete(String groupUid, JwtUser jwtUser) {
        RestResult<TGroup> checkResult = check(groupUid,jwtUser.getUserId());
        if(!checkResult.isSuccess()){
            return checkResult;
        }
        TGroup group = checkResult.getResult();
        TCommunity tCommunity = tCommunityService.queryByUid(group.getCommunityUid());
        if(tCommunity==null){
            return RestResult.generic(RestResultCode.ERR_COMMUNITY_NOT_EXIT);
        }
        String joinChannelUid = tCommunity.getJoinChannelUid();
        TChannel tChannel = TChannelService.queryByUid(joinChannelUid);
        if(tChannel.getGroupUid().equals(groupUid)){
            return RestResult.generic(RestResultCode.ERR_GROUP_DEFAULT_JOIN);
        }
        redisUtils.del(String.format(CommunityConstant.GROUP_INFO, groupUid));
        group.setDelFlag(1);
        TGroupMapper.updateById(group);
        sendGroupDeleteMessage(tCommunity.getUid(),Lists.newArrayList(group.getUid()),jwtUser.getUserId());
        TChannelService.deleteByGroupUids(Lists.newArrayList(groupUid));
        return RestResult.success();
    }

    @Override
    public void sendGroupDeleteMessage(String communityUid,List<String> groupUids,String userId){
        List<TChannel> channels = TChannelService.queryByGroupUids(groupUids);
        List<String> channelUids = Lists.newArrayList();
        if(!CollectionUtils.isEmpty(channels)){
            channelUids = channels.stream().map(TChannel::getUid).collect(Collectors.toList());
        }
        communityMessageUtil.sendCommunityChangeMessage(userId,communityUid,channelUids);
    }

    @Override
    public RestResult update(TGroupUpdateReq params, JwtUser jwtUser) {
        if(params==null||StringUtils.isBlank(params.getName())||StringUtils.isBlank(params.getUid())){
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        RestResult<TGroup> check = check(params.getUid(), jwtUser.getUserId());
        if(!check.isSuccess()){
            return check;
        }
        redisUtils.del(String.format(CommunityConstant.GROUP_INFO, params.getUid()));
        TGroup group = check.getResult();
        group.setName(params.getName());
        TGroupMapper.updateById(group);
        return RestResult.success();
    }

    @Override
    public void insert(TGroup group) {
        TGroupMapper.insert(group);
    }

    @Override
    public void deleteByCommunityUid(String communityUid) {
        if(StringUtils.isBlank(communityUid)){
            return;
        }
        // 这里没有删缓存，因为不会再查到了
        TGroup group = TGroup.builder().delFlag(1).build();
        TGroupMapper.update(group,new QueryWrapper<TGroup>().lambda().eq(TGroup::getCommunityUid,communityUid));
    }

    @Override
    public void batchUpate(List<TGroup> updateGroups) {
        if(CollectionUtils.isEmpty(updateGroups)){
            return;
        }
        // 删缓存
        for(TGroup group : updateGroups){
            redisUtils.del(String.format(CommunityConstant.GROUP_INFO, group.getUid()));
        }
        TGroupMapper.batchUpdate(updateGroups);
    }

    @Override
    public TGroup queryByCommunityUidAndName(String communityUid, String name) {
        if(StringUtils.isBlank(name)||StringUtils.isBlank(communityUid)){
            return null;
        }
        LambdaQueryWrapper<TGroup> queryWrapper = new QueryWrapper<TGroup>().lambda()
                .eq(TGroup::getCommunityUid,communityUid)
                .eq(TGroup::getName, name).eq(TGroup::getDelFlag, 0);
        return TGroupMapper.selectOne(queryWrapper);
    }

    /**
     * 校验
     * @param uid
     * @param userId
     * @return
     */
    private RestResult<TGroup> check(String uid,String userId){
        if(StringUtils.isBlank(uid)||StringUtils.isBlank(userId)){
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        TGroup group = queryByUid(uid);
        if(group==null){
            return RestResult.generic(RestResultCode.ERR_COMMUNITY_NOT_EXIT);
        }
        if(!group.getCreator().equals(userId)){
            return RestResult.generic(RestResultCode.ERR_ACCESS_DENIED);
        }
        return RestResult.success(group);
    }


}