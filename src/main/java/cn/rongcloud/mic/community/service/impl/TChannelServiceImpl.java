package cn.rongcloud.mic.community.service.impl;

import cn.rongcloud.common.im.GroupHelper;
import cn.rongcloud.common.im.pojos.IMApiResultInfo;
import cn.rongcloud.common.utils.IdentifierUtils;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.redis.RedisUtils;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.rest.RestResultCode;
import cn.rongcloud.mic.common.utils.CoverUtil;
import cn.rongcloud.mic.community.constant.CommunityConstant;
import cn.rongcloud.mic.community.dto.TChannelDTO;
import cn.rongcloud.mic.community.dto.TChannelSaveReq;
import cn.rongcloud.mic.community.dto.TChannelUpdateReq;
import cn.rongcloud.mic.community.entity.TChannel;
import cn.rongcloud.mic.community.entity.TCommunity;
import cn.rongcloud.mic.community.mapper.TChannelMapper;
import cn.rongcloud.mic.community.message.CommunityMessageUtil;
import cn.rongcloud.mic.community.service.TChannelService;
import cn.rongcloud.mic.community.service.TCommunityService;
import cn.rongcloud.mic.community.vo.TChannelVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TChannelServiceImpl implements TChannelService {

    @Resource
    private TChannelMapper TChannelMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private GroupHelper groupHelper;

    @Autowired
    private CommunityMessageUtil communityMessageUtil;

    @Autowired
    private TCommunityService tCommunityService;


    @Override
    public RestResult<IPage<TChannelVO>> queryPage(TChannelDTO params) {
        Page<TChannel> pageParam = new Page<>();
        pageParam.setCurrent(params.getPageNum());
        pageParam.setSize(params.getPageSize());
        IPage pageResult = TChannelMapper.selectPage(pageParam, new QueryWrapper<TChannel>().lambda().eq(TChannel::getDelFlag,0));
        List<TChannel> records = pageResult.getRecords();
        List<TChannelVO> pageVOS = CoverUtil.sourceToTarget(records, TChannelVO.class);
        pageResult.setRecords(pageVOS);
        return RestResult.success(pageResult);
    }

    @Override
    public TChannel queryByUid(String uid) {
        if(StringUtils.isBlank(uid)){
            return null;
        }
        // 缓存拿
        String channelInfoKey = String.format(CommunityConstant.CHANNEL_INFO, uid);
        TChannel entity = (TChannel)redisUtils.get(channelInfoKey);
        if(entity!=null){
            return entity;
        }
        entity = TChannelMapper.selectOne(new QueryWrapper<TChannel>().lambda().eq(TChannel::getUid,uid).eq(TChannel::getDelFlag,0));
        if(entity!=null){
            redisUtils.set(channelInfoKey,entity,CommunityConstant.INFO_TIME);
        }
        return entity;
    }

    @Override
    public List<TChannel> queryByCommunityUid(String communityUid) {
        return TChannelMapper.selectList(new QueryWrapper<TChannel>().lambda().eq(TChannel::getCommunityUid,communityUid).eq(TChannel::getDelFlag,0));
    }

    @Override
    public RestResult<String> save(TChannelSaveReq params, JwtUser jwtUser) {

        if(StringUtils.isBlank(params.getName())||StringUtils.isBlank(params.getCommunityUid())){
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        if(params.getName().length()>16){
            return RestResult.generic(RestResultCode.ERR_CREATE_CHANNEL_NAME);
        }

        TChannel nameCheck = this.queryByCommunityUidAndName(params.getCommunityUid(),params.getName());
        if(nameCheck!=null){
            return RestResult.generic(RestResultCode.ERR_CHANNEL_NAME);
        }

        // IM 同步
        String uuid = IdentifierUtils.getGUID();
        IMApiResultInfo resultInfo = groupHelper.createChannel(params.getCommunityUid(), uuid);
        if(resultInfo==null||!resultInfo.isSuccess()){
            return RestResult.generic(RestResultCode.ERR_OTHER);
        }
        Integer sort =null;
        if(StringUtils.isNotBlank(params.getGroupUid())){
            sort = TChannelMapper.maxSortByGroupUid(params.getGroupUid());
        }else{
            sort = TChannelMapper.maxSortByCommuntiyUid(params.getCommunityUid());
        }
        sort = sort==null?0:sort;
        TChannel channel = TChannel.builder().uid(uuid).communityUid(params.getCommunityUid()).groupUid(params.getGroupUid())
                .remark("欢迎来到频道"+params.getName())
                .name(params.getName()).creator(jwtUser.getUserId()).sort(sort+1).build();
        TChannelMapper.insert(channel);
        communityMessageUtil.sendCommunityChangeMessage(jwtUser.getUserId(),params.getCommunityUid(), Lists.newArrayList());
        return RestResult.success(uuid);
    }

    @Override
    public RestResult delete(String channelUid, JwtUser jwtUser) {
        RestResult<TChannel> check = check(channelUid, jwtUser.getUserId());
        if(!check.isSuccess()){
            return check;
        }
        TChannel channel = check.getResult();
        TCommunity community = tCommunityService.queryByUid(channel.getCommunityUid());
        if(channelUid.equals(community.getJoinChannelUid())){
            return RestResult.generic(RestResultCode.ERR_CHANNEL_DEFAULT_JOIN);
        }
        if(channelUid.equals(community.getMsgChannelUid())){
            community.setMsgChannelUid("");
            tCommunityService.updateById(community);
        }
        redisUtils.del(String.format(CommunityConstant.CHANNEL_INFO, channelUid));
        // IM 删除 频道
        IMApiResultInfo resultInfo = groupHelper.delChannel(channel.getCommunityUid(), channel.getUid());
        if(resultInfo==null||!resultInfo.isSuccess()){
            return RestResult.generic(RestResultCode.ERR_OTHER);
        }
        // 删除频道
        channel.setDelFlag(1);
        TChannelMapper.updateById(channel);
        communityMessageUtil.sendCommunityChangeMessage(jwtUser.getUserId(),channel.getCommunityUid(), Lists.newArrayList(channel.getUid()));
        return RestResult.success();
    }

    @Override
    public RestResult update(TChannelUpdateReq params, JwtUser jwtUser) {
        RestResult<TChannel> check = check(params.getUid(), jwtUser.getUserId());
        if(!check.isSuccess()){
            return check;
        }
        redisUtils.del(String.format(CommunityConstant.CHANNEL_INFO, params.getUid()));
        TChannel channel = check.getResult();

        if(StringUtils.isNotBlank(params.getName())){
            channel.setName(params.getName());
        }
        if(StringUtils.isNotBlank(params.getRemark())){
            channel.setRemark(params.getRemark());
        }

        TChannelMapper.updateById(channel);
        return RestResult.success();
    }

    @Override
    public void insert(TChannel channel) {
        TChannelMapper.insert(channel);
    }

    @Override
    public void deleteByCommunityUid(String communityUid) {
        // 这里没有删缓存，因为不会再查到了
        TChannel channel = TChannel.builder().delFlag(1).build();
        TChannelMapper.update(channel,new QueryWrapper<TChannel>().lambda().eq(TChannel::getCommunityUid,communityUid));
    }

    @Override
    public void deleteByGroupUids(List<String> groupUids) {
        // 这里没有删缓存，因为不会再查到了
        TChannel channel = TChannel.builder().delFlag(1).build();
        TChannelMapper.update(channel,new QueryWrapper<TChannel>().lambda().in(TChannel::getGroupUid,groupUids));
    }

    @Override
    public void batchUpdate(List<TChannel> updateChannels) {
        if(CollectionUtils.isEmpty(updateChannels)){
            return;
        }
        // 删缓存
        for(TChannel channel : updateChannels){
            redisUtils.del(String.format(CommunityConstant.CHANNEL_INFO, channel.getUid()));
        }
        TChannelMapper.batchUpdate(updateChannels);
    }

    @Override
    public RestResult<TChannelVO> detail(String channelUid, JwtUser jwtUser) {
        if(StringUtils.isBlank(channelUid)){
            return RestResult.success();
        }
        TChannelVO vo = new TChannelVO();
        TChannel channel = queryByUid(channelUid);
        BeanUtils.copyProperties(channel,vo);
        return RestResult.success(vo);
    }

    @Override
    public TChannel queryByCommunityUidAndName(String communityUid,String name) {
        if(StringUtils.isBlank(name)||StringUtils.isBlank(communityUid)){
            return null;
        }
        LambdaQueryWrapper<TChannel> queryWrapper = new QueryWrapper<TChannel>().lambda()
                .eq(TChannel::getCommunityUid,communityUid)
                .eq(TChannel::getName, name).eq(TChannel::getDelFlag, 0);
        return TChannelMapper.selectOne(queryWrapper);
    }

    @Override
    public List<TChannel> queryByGroupUids(List<String> groupUids) {
        if(CollectionUtils.isEmpty(groupUids)){
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<TChannel> queryWrapper = new QueryWrapper<TChannel>().lambda()
                .in(TChannel::getGroupUid,groupUids)
                .eq(TChannel::getDelFlag, 0);
        return TChannelMapper.selectList(queryWrapper);
    }

    /**
     * 校验
     * @param uid
     * @param userId
     * @return
     */
    private RestResult<TChannel> check(String uid,String userId){
        if(StringUtils.isBlank(uid)||StringUtils.isBlank(userId)){
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        TChannel channel = queryByUid(uid);
        if(channel==null){
            return RestResult.generic(RestResultCode.ERR_COMMUNITY_NOT_EXIT);
        }
        if(!channel.getCreator().equals(userId)){
            return RestResult.generic(RestResultCode.ERR_ACCESS_DENIED);
        }
        return RestResult.success(channel);
    }

}