package cn.rongcloud.mic.community.service.impl;


import cn.rongcloud.common.im.GroupHelper;
import cn.rongcloud.common.im.pojos.IMApiResultInfo;
import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.common.utils.IdentifierUtils;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.redis.RedisUtils;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.rest.RestResultCode;
import cn.rongcloud.mic.community.constant.CommunityConstant;
import cn.rongcloud.mic.community.dto.CommunityUserCount;
import cn.rongcloud.mic.community.dto.TChannelDetailResp;
import cn.rongcloud.mic.community.dto.TCommunityDTO;
import cn.rongcloud.mic.community.dto.TCommunityDetailResp;
import cn.rongcloud.mic.community.dto.TCommunitySaveReq;
import cn.rongcloud.mic.community.dto.TCommunityUpdateReq;
import cn.rongcloud.mic.community.dto.TGroupDetailResp;
import cn.rongcloud.mic.community.dto.TUserChannelSettingDTO;
import cn.rongcloud.mic.community.entity.TChannel;
import cn.rongcloud.mic.community.entity.TCommunity;
import cn.rongcloud.mic.community.entity.TCommunityUser;
import cn.rongcloud.mic.community.entity.TGroup;
import cn.rongcloud.mic.community.enums.NeedAuditEnum;
import cn.rongcloud.mic.community.mapper.TCommunityMapper;
import cn.rongcloud.mic.community.message.CommunityMessageUtil;
import cn.rongcloud.mic.community.service.TChannelService;
import cn.rongcloud.mic.community.service.TCommunityService;
import cn.rongcloud.mic.community.service.TCommunityUserService;
import cn.rongcloud.mic.community.service.TGroupService;
import cn.rongcloud.mic.community.vo.TCommunityVO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TCommunityServiceImpl implements TCommunityService {

    @Resource
    private TCommunityMapper TCommunityMapper;

    @Autowired
    private TGroupService TGroupService;

    @Autowired
    private TChannelService TChannelService;

    @Autowired
    private TCommunityUserService TCommunityUserService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private GroupHelper groupHelper;

    @Autowired
    private CommunityMessageUtil communityMessageUtil;

    @Override
    public RestResult<IPage<TCommunityVO>> queryPage(TCommunityDTO params) {
        Page<TCommunity> pageParam = new Page<>();
        pageParam.setCurrent(params.getPageNum());
        pageParam.setSize(params.getPageSize());
        LambdaQueryWrapper<TCommunity> queryWrapper = new QueryWrapper<TCommunity>().lambda()
                .eq(StringUtils.isNotEmpty(params.getName()),TCommunity::getName,params.getName())
                .eq(params.getId()!=null,TCommunity::getId,params.getId())
                .eq(TCommunity::getDelFlag, 0).orderByDesc(TCommunity::getId);
        IPage pageResult = TCommunityMapper.selectPage(pageParam,queryWrapper);
        List<TCommunity> records = pageResult.getRecords();
        List<TCommunityVO> pageVOS = Lists.newArrayList();
        if(!CollectionUtils.isEmpty(records)){
            List<String> communityUids = records.stream().map(TCommunity::getUid).collect(Collectors.toList());
            List<CommunityUserCount> communityUserCounts = TCommunityUserService.queryUserCountByCommunityUids(communityUids);
            Map<String, CommunityUserCount> countMap = communityUserCounts.stream().collect(Collectors.toMap(CommunityUserCount::getCommunityUid, o -> o, (k1, k2) -> k1));
            records.stream().forEach(item->{
                TCommunityVO vo = new TCommunityVO();
                BeanUtils.copyProperties(item,vo);
                vo.setCommunityUid(item.getUid());
                CommunityUserCount communityUserCount = countMap.get(item.getUid());
                vo.setPersonCount(communityUserCount==null?0:communityUserCount.getCount());
                pageVOS.add(vo);
            });
        }

        pageResult.setRecords(pageVOS);
        return RestResult.success(pageResult);
    }

    @Override
    public TCommunity queryByUid(String uid) {
        if(StringUtils.isBlank(uid)){
            return null;
        }
        // 缓存拿
        String communityInfoKey = String.format(CommunityConstant.COMMUNITY_INFO, uid);
        TCommunity community = (TCommunity)redisUtils.get(communityInfoKey);
        if(community!=null){
            return community;
        }
        // 数据库拿
        community = TCommunityMapper.selectOne(new QueryWrapper<TCommunity>().lambda().eq(TCommunity::getUid, uid).eq(TCommunity::getDelFlag,0));

        // 缓存
        if(community!=null){
            redisUtils.set(communityInfoKey,community,CommunityConstant.INFO_TIME);
        }
        return community;
    }

    @Override
    public RestResult<String> save(TCommunitySaveReq params, JwtUser jwtUser) {

        if(StringUtils.isBlank(params.getName())||StringUtils.isBlank(params.getPortrait())){
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        if(params.getName().length()>16){
            return RestResult.generic(RestResultCode.ERR_CREATE_COMMUNITY_NAME);
        }
        List<TCommunity> tCommunities = queryByCreator(jwtUser.getUserId());
        if(!CollectionUtils.isEmpty(tCommunities)&&tCommunities.size()>=3){
            return RestResult.generic(RestResultCode.ERR_CREATE_COMMUNITY_LIMIT);
        }

        // 新建社区
        String uid = IdentifierUtils.uuid();
        // 后期如果这个接口太慢，那么就用异步吧
       /* CompletableFuture.runAsync(() -> {
                defalutCreateChannel(uid, jwtUser);
        });*/
        String defaultChannelUid = IdentifierUtils.getGUID();
        IMApiResultInfo resultInfo = groupHelper.createGroup(uid, params.getName(), jwtUser.getUserId());
        if(resultInfo==null||!resultInfo.isSuccess()){
            return RestResult.generic(RestResultCode.ERR_OTHER);
        }
        TCommunity community = TCommunity.builder().name(params.getName()).coverUrl(params.getPortrait())
                .joinChannelUid(defaultChannelUid).msgChannelUid(defaultChannelUid)
                .portrait(params.getPortrait()).creator(jwtUser.getUserId()).uid(uid).build();
        TCommunityMapper.insert(community);
        IMApiResultInfo resultInfo1 = groupHelper.joinGroup(uid, jwtUser.getUserId());
        if(resultInfo1==null||!resultInfo1.isSuccess()){
            return RestResult.generic(RestResultCode.ERR_OTHER);
        }
        TCommunityUserService.join(uid,jwtUser);

        // 同步IM
        return defalutCreateChannel(uid, jwtUser,defaultChannelUid);
    }

    private RestResult<String> defalutCreateChannel(String uid,JwtUser jwtUser,String defaultChannelUid){
        // 建两个默认分组
        String group1Uid = IdentifierUtils.uuid();
        TGroup group1 = TGroup.builder().communityUid(uid).name("分组1")
                .creator(jwtUser.getUserId()).sort(1).uid(group1Uid).build();

        TGroupService.insert(group1);
        String group2Uid = IdentifierUtils.uuid();
        TGroup group2 = TGroup.builder().communityUid(uid).name("分组2")
                .creator(jwtUser.getUserId()).sort(2).uid(group2Uid).build();
        TGroupService.insert(group2);
        // 建两个默认频道
        String channelUid2 = IdentifierUtils.getGUID();
        IMApiResultInfo channelResult = groupHelper.createChannel(uid, defaultChannelUid);
        IMApiResultInfo channel2Result = groupHelper.createChannel(uid, channelUid2);
        if(channelResult==null||!channelResult.isSuccess()){
            return RestResult.generic(RestResultCode.ERR_OTHER);
        }
        if(channel2Result==null||!channel2Result.isSuccess()){
            return RestResult.generic(RestResultCode.ERR_OTHER);
        }
        TChannel channel1 = TChannel.builder().communityUid(uid).groupUid(group1Uid).name("频道1")
                .uid(defaultChannelUid).remark("欢迎来到"+"频道1")
                .creator(jwtUser.getUserId()).sort(1).build();
        TChannelService.insert(channel1);
        TChannel channel2 = TChannel.builder().communityUid(uid).groupUid(group2Uid).name("频道2")
                .uid(channelUid2).remark("欢迎来到"+"频道2")
                .creator(jwtUser.getUserId()).sort(2).build();
        TChannelService.insert(channel2);
        return RestResult.success(uid);
    }
    @Override
    public RestResult delete(String communityUid, JwtUser jwtUser) {
        RestResult<TCommunity> checkResult = check(communityUid, jwtUser.getUserId());
        if(!checkResult.isSuccess()){
            return checkResult;
        }
        TCommunity community = checkResult.getResult();
        // 删缓存
        redisUtils.del(String.format(CommunityConstant.COMMUNITY_INFO, communityUid));


        List<TCommunityUser> communityUsers = TCommunityUserService.queryByCommunityUid(community.getUid());
        List<String> userIds = communityUsers.stream().map(TCommunityUser::getUserUid).collect(Collectors.toList());


        // 删除社区
        community.setDelFlag(1);
        TCommunityMapper.updateById(community);
        // 删除分组
        TGroupService.deleteByCommunityUid(communityUid);
        // 删除频道
        TChannelService.deleteByCommunityUid(communityUid);
        // 删除社区用户
        TCommunityUserService.deleteByCommunityUid(communityUid);

        String message = String.format("社区【%s】已解散",community.getName());
        communityMessageUtil.sendCommunityNoticeMessage(jwtUser.getUserId(),userIds,community.getUid(),message,7);
        communityMessageUtil.sendCommunityDeleteMessage(jwtUser.getUserId(),communityUid);
        CompletableFuture.runAsync(()->{
            this.delayDeleteCommunity(communityUid);
        });
        return RestResult.success();
    }

    /**
     *   IM 解散超级群 延迟解散，原因：im的已知问题，实时会导致消息收不到
     * @param communityUid
     */
    private void delayDeleteCommunity(String communityUid){
        try {
            log.info("delay delete community start communityUid:{}",communityUid);
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.info("delay delete community error communityUid:{}",communityUid);
            e.printStackTrace();
        }
        IMApiResultInfo resultInfo = groupHelper.delGroup(communityUid);
        log.info("delay delete community communityUid:{}, result:{}", communityUid,GsonUtil.toJson(resultInfo));
    }

    @Override
    public RestResult<TCommunityDetailResp> detail(String communityUid, JwtUser jwtUser) {
        if(StringUtils.isBlank(communityUid)){
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        TCommunity community = queryByUid(communityUid);
        TCommunityDetailResp communityDetailResp = TCommunityDetailResp.builder()
                .id(community.getId()).joinChannelUid(community.getJoinChannelUid()).msgChannelUid(community.getMsgChannelUid())
                .needAudit(community.getNeedAudit()).noticeType(community.getNoticeType())
                .creator(community.getCreator())
                .uid(communityUid).name(community.getName()).coverUrl(community.getCoverUrl())
                .portrait(community.getPortrait()).remark(community.getRemark()).build();
        List<TGroup> tGroups = TGroupService.queryByCommunityUid(communityUid);

        // 获取用户设置的通知类型
        TCommunityUser communityUser = TCommunityUserService.queryByUserIdAndCommunityUid(jwtUser.getUserId(), communityUid);

        List<CommunityUserCount> communityUserCounts = TCommunityUserService.queryUserCountByCommunityUids(Lists.newArrayList(communityUid));
        communityDetailResp.setPersonCount(communityUserCounts.get(0).getCount());
        Integer auditStatus = 0;
        // 处理 auditStatus
        List<TUserChannelSettingDTO> dtos = null;
        TCommunityDetailResp.CommunityUser communityUser1 = new TCommunityDetailResp.CommunityUser();
        communityUser1.setNoticeType(community.getNoticeType());
        Integer noticeType = community.getNoticeType();
        if(communityUser!=null){
            communityUser1.setNickName(communityUser.getNickName());
            if(communityUser.getStatus().intValue()==1||communityUser.getStatus().intValue()==2||communityUser.getStatus().intValue()==3){
                auditStatus=communityUser.getStatus();
            }
            if(communityUser.getNoticeType().intValue()!=-1){
                noticeType = communityUser.getNoticeType();
            }
            communityUser1.setShutUp(communityUser.getShutUp());
            dtos = JSON.parseArray(communityUser.getChannelSetting(), TUserChannelSettingDTO.class);
        }
        communityUser1.setNoticeType(noticeType);
        communityUser1.setAuditStatus(auditStatus);
        communityDetailResp.setCommunityUser(communityUser1);
        Map<String,TUserChannelSettingDTO> channelUidMap = dtos==null? Maps.newHashMap():dtos.stream().collect(Collectors.toMap(TUserChannelSettingDTO::getChannelUid, o -> o, (k1, k2) -> k1));

        List<TChannel> tChannels = TChannelService.queryByCommunityUid(communityUid);
        Map<String, List<TChannel>> groupMap = tChannels.stream().collect(Collectors.groupingBy(TChannel::getGroupUid, Collectors.toList()));
        List<TGroupDetailResp> groupDetailResps = Lists.newArrayList();
        for(TGroup group : tGroups){
            String groupUid = group.getUid();
            TGroupDetailResp groupDetailResp = TGroupDetailResp.builder().uid(groupUid).name(group.getName()).sort(group.getSort()).build();
            List<TChannel> dbChannels = groupMap.get(groupUid);
            List<TChannelDetailResp> inChannelDetailResps = covert2VO(dbChannels, channelUidMap, noticeType);
            groupDetailResp.setChannelList(inChannelDetailResps);
            groupDetailResps.add(groupDetailResp);
        }
        // 没有分组的
        List<TChannel> channels = groupMap.get("");
        List<TChannelDetailResp> outChannelDetailResps = covert2VO(channels, channelUidMap, noticeType);
        communityDetailResp.setChannelList(outChannelDetailResps);
        // 升序
        groupDetailResps.sort((o1, o2) -> o1.getSort() - o2.getSort());
        communityDetailResp.setGroupList(groupDetailResps);
        return RestResult.success(communityDetailResp);
    }

    private List<TChannelDetailResp> covert2VO( List<TChannel> dbChannels,Map<String,TUserChannelSettingDTO> channelUidMap,Integer noticeType){
        List<TChannelDetailResp> channelDetailResps = Lists.newArrayList();
        if(!CollectionUtils.isEmpty(dbChannels)){
            for(TChannel dbChannel : dbChannels){
                TChannelDetailResp channelDetailResp = new TChannelDetailResp();
                BeanUtils.copyProperties(dbChannel,channelDetailResp);
                TUserChannelSettingDTO dto = channelUidMap.get(dbChannel.getUid());
                if(dto==null||dto.getNoticeType().intValue()==-1){
                    channelDetailResp.setNoticeType(noticeType);
                }else{
                    channelDetailResp.setNoticeType(dto.getNoticeType());
                }
                channelDetailResps.add(channelDetailResp);

            }
            // 升序
            channelDetailResps.sort((o1, o2) -> o1.getSort() - o2.getSort());
        }
        return channelDetailResps;
    }
    @Override
    public RestResult saveAll(TCommunityDetailResp reqParam, JwtUser jwtUser) {

        RestResult<Integer> hasDeleteResult = null;
        // 删缓存
        //redisUtils.del(String.format(CommunityConstant.COMMUNITY_INFO, reqParam.getUid()));
        if(reqParam.getUpdateType().intValue()==3){
            TCommunityUpdateReq params = new TCommunityUpdateReq();
            BeanUtils.copyProperties(reqParam,params);
            this.update(params,jwtUser);
        }else{
            // 获取详情
            RestResult<TCommunityDetailResp> resp = this.detail(reqParam.getUid(), jwtUser);
            if(!resp.isSuccess()){
                return resp;
            }

            if(reqParam.getUpdateType().intValue()==1){
                hasDeleteResult = handleGroup(resp, reqParam);

            }else if(reqParam.getUpdateType().intValue()==2){
                hasDeleteResult = handleChannel(resp,reqParam);
            }

        }
        if(hasDeleteResult==null||hasDeleteResult.getResult().intValue()==0){
            communityMessageUtil.sendCommunityChangeMessage(jwtUser.getUserId(),reqParam.getUid(), Lists.newArrayList());
        }

        return RestResult.success();
    }

    /**
     * 整体保存-处理分组
     * @param resp
     * @param reqParam
     * @return
     */
    private RestResult<Integer> handleGroup(RestResult<TCommunityDetailResp> resp,TCommunityDetailResp reqParam){

        Integer hasDelete = 0;
        // 分组
        List<TGroupDetailResp> dbGroupList = resp.getResult().getGroupList();
        Map<String, TGroupDetailResp> reqGroupUidMap =  reqParam.getGroupList().stream().collect(Collectors.toMap(item -> item.getUid(), item -> item));
        List<TGroup> updateGroups = Lists.newArrayList();
        int groupSort = 0;
        List<String> deleteGroupUids = Lists.newArrayList();
        for(TGroupDetailResp dbGroup : dbGroupList) {
            String groupUid = dbGroup.getUid();
            if (reqGroupUidMap.containsKey(groupUid)) {

            } else {
                // 删除分组
                TGroup group = TGroup.builder().uid(dbGroup.getUid()).sort(dbGroup.getSort()).name(dbGroup.getName()).delFlag(1).build();
                updateGroups.add(group);
                deleteGroupUids.add(group.getUid());
                hasDelete =1;
            }
        }
        List<TGroupDetailResp> reqGroupList = reqParam.getGroupList();
        for(TGroupDetailResp reqGroup: reqGroupList){
            TGroup group = TGroup.builder().uid(reqGroup.getUid()).sort(++groupSort).name(reqGroup.getName()).delFlag(0).build();
            updateGroups.add(group);
        }
        TGroupService.batchUpate(updateGroups);
        if(!CollectionUtils.isEmpty(deleteGroupUids)){
            CompletableFuture.runAsync(()->{
                TGroupService.sendGroupDeleteMessage(reqParam.getUid(),deleteGroupUids,reqParam.getCreator());
            });
            // 这里没有删除分组对应下的频道，因为不会导致问题
            TChannelService.deleteByGroupUids(deleteGroupUids);
        }

        return RestResult.success(hasDelete);
    }

    /**
     * 整体保存-处理频道
     * @param resp
     * @param reqParam
     * @return
     */
    private RestResult<Integer> handleChannel(RestResult<TCommunityDetailResp> resp,TCommunityDetailResp reqParam){
        Integer hasDelete = 0;
        // 所有的频道
        List<TChannelDetailResp> dbAllChannelList = Lists.newArrayList();
        List<TChannelDetailResp> dbOutChannelList = resp.getResult().getChannelList();
        dbAllChannelList.addAll(dbOutChannelList);
        resp.getResult().getGroupList().stream().forEach(item->{
            List<TChannelDetailResp> channelList = item.getChannelList();
            dbAllChannelList.addAll(channelList);
        });

        List<TChannelDetailResp> reqInChannelList = Lists.newArrayList();
        reqParam.getGroupList().stream().forEach(item->{
            // 如果是外层频道移动到里层频道，那么他的groupUid 要给赋值
            List<TChannelDetailResp> channelList = item.getChannelList();
            for(TChannelDetailResp channelDetailResp : channelList){
                channelDetailResp.setGroupUid(item.getUid());
            }
            reqInChannelList.addAll(channelList);
        });
        int outChannelSort = 0;
        Map<String, TChannelDetailResp> reqOutChannelMap = Maps.newHashMap();
        if(!CollectionUtils.isEmpty(reqParam.getChannelList())){
            for(TChannelDetailResp detailResp:reqParam.getChannelList()){
                String uid = detailResp.getUid();
                detailResp.setSort(++outChannelSort);
                reqOutChannelMap.put(uid,detailResp);
            }
        }
        int inChannelSort = 0;
        Map<String, TChannelDetailResp> reqInChannelMap = Maps.newHashMap();
        if(!CollectionUtils.isEmpty(reqInChannelList)){
            for(TChannelDetailResp detailResp:reqInChannelList){
                String uid = detailResp.getUid();
                detailResp.setSort(++inChannelSort);
                reqInChannelMap.put(uid,detailResp);
            }
        }

        // 处理频道
        List<TChannel> updateChannels = Lists.newArrayList();
        List<String> deleteChannelUids = Lists.newArrayList();
        for(TChannelDetailResp dbChannel : dbAllChannelList){
            String uid = dbChannel.getUid();
            if(reqOutChannelMap.containsKey(uid)){
                // 说明在外层频道里
                TChannelDetailResp channelDetailResp = reqOutChannelMap.get(uid);
                // 修改的频道
                TChannel channel = TChannel.builder().uid(channelDetailResp.getUid()).name(channelDetailResp.getName())
                        .groupUid("").sort(channelDetailResp.getSort()).delFlag(0).build();
                updateChannels.add(channel);
            }else if (reqInChannelMap.containsKey(uid)){
                TChannelDetailResp channelDetailResp = reqInChannelMap.get(uid);
                TChannel channel = TChannel.builder().uid(channelDetailResp.getUid()).name(channelDetailResp.getName())
                        .groupUid(channelDetailResp.getGroupUid()).sort(channelDetailResp.getSort()).delFlag(0).build();
                updateChannels.add(channel);
            }else{
                hasDelete=1;
                // IM 删分组
                groupHelper.delChannel(resp.getResult().getUid(),dbChannel.getGroupUid());
                // 删除的频道
                TChannel channel = TChannel.builder().uid(dbChannel.getUid()).name(dbChannel.getName())
                        .groupUid(dbChannel.getGroupUid()).sort(dbChannel.getSort()).delFlag(1).build();
                updateChannels.add(channel);
                deleteChannelUids.add(channel.getUid());
            }
        }
        TChannelService.batchUpdate(updateChannels);
        if(!CollectionUtils.isEmpty(deleteChannelUids)){
            communityMessageUtil.sendCommunityChangeMessage(reqParam.getCreator(),reqParam.getUid(),deleteChannelUids);
        }
        return RestResult.success(hasDelete);
    }

    @Override
    public RestResult update(TCommunityUpdateReq params, JwtUser jwtUser) {
        RestResult<TCommunity> checkResult = check(params.getUid(),jwtUser.getUserId());
        if(!checkResult.isSuccess()){
            return checkResult;
        }
        TCommunity community = checkResult.getResult();
        // 删缓存
        //redisUtils.del(String.format(CommunityConstant.COMMUNITY_INFO, params.getUid()));

        if(StringUtils.isNotBlank(params.getName())&&!community.getName().equals(params.getName())){
            community.setName(params.getName());
            IMApiResultInfo resultInfo = groupHelper.updateGroup(params.getUid(), params.getName());
            if(resultInfo==null||!resultInfo.isSuccess()){
                return RestResult.generic(RestResultCode.ERR_OTHER);
            }
        }
        if(StringUtils.isNotBlank(params.getCoverUrl())){
            community.setCoverUrl(params.getCoverUrl());
        }
        if(StringUtils.isNotBlank(params.getPortrait())){
            community.setPortrait(params.getPortrait());
        }
        if(StringUtils.isNotBlank(params.getRemark())){
            community.setRemark(params.getRemark());
        }
        if(params.getNeedAudit()!=null){
            if(params.getNeedAudit().intValue()== NeedAuditEnum.NO.getValue()&&community.getNeedAudit().intValue()==NeedAuditEnum.YES.getValue()){
                // 由需要审核变为不需要，需要处理一下在审核中的人
                TCommunityUserService.updateJoinByCommunityUid(community.getUid());
            }
            community.setNeedAudit(params.getNeedAudit());
        }
        if(params.getNoticeType()!=null){
            community.setNoticeType(params.getNoticeType());
        }
        if(StringUtils.isNotBlank(params.getJoinChannelUid())){
            community.setJoinChannelUid(params.getJoinChannelUid());
        }
        if(StringUtils.isNotBlank(params.getMsgChannelUid())){
            community.setMsgChannelUid(params.getMsgChannelUid());
        }
        TCommunityMapper.updateById(community);
        redisUtils.set(String.format(CommunityConstant.COMMUNITY_INFO, params.getUid()),community,CommunityConstant.INFO_TIME);
        return RestResult.success();
    }

    @Override
    public List<TCommunity> queryByCreator(String creator) {
        return TCommunityMapper.selectList(new QueryWrapper<TCommunity>().lambda().eq(TCommunity::getCreator, creator)
                .eq(TCommunity::getDelFlag,0));
    }

    @Override
    public int updateById(TCommunity community) {
       return TCommunityMapper.updateById(community);
    }

    /**
     * 校验
     * @param uid
     * @param userId
     * @return
     */
    private RestResult<TCommunity> check(String uid,String userId){
        if(StringUtils.isBlank(uid)||StringUtils.isBlank(userId)){
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        TCommunity community = queryByUid(uid);
        if(community==null){
            return RestResult.generic(RestResultCode.ERR_COMMUNITY_NOT_EXIT);
        }
        if(!community.getCreator().equals(userId)){
            return RestResult.generic(RestResultCode.ERR_ACCESS_DENIED);
        }
        return RestResult.success(community);
    }

}