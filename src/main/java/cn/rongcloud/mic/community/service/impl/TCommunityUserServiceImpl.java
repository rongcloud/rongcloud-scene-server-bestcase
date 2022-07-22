package cn.rongcloud.mic.community.service.impl;


import cn.rongcloud.common.im.GroupHelper;
import cn.rongcloud.common.im.IMHelper;
import cn.rongcloud.common.im.pojos.IMApiResultInfo;
import cn.rongcloud.common.im.pojos.IMUserStatusResultInfo;
import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.common.utils.IMSignUtil;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.page.PageParam;
import cn.rongcloud.mic.common.redis.RedisUtils;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.rest.RestResultCode;
import cn.rongcloud.mic.community.constant.CommunityConstant;
import cn.rongcloud.mic.community.dto.*;
import cn.rongcloud.mic.community.entity.TCommunity;
import cn.rongcloud.mic.community.entity.TCommunityUser;
import cn.rongcloud.mic.community.enums.ChannelMessageTypeEnum;
import cn.rongcloud.mic.community.enums.CommunityUserStatusEnum;
import cn.rongcloud.mic.community.enums.NeedAuditEnum;
import cn.rongcloud.mic.community.mapper.TCommunityUserMapper;
import cn.rongcloud.mic.community.message.CommunityMessageUtil;
import cn.rongcloud.mic.community.service.TCommunityService;
import cn.rongcloud.mic.community.service.TCommunityUserService;
import cn.rongcloud.mic.community.vo.TCommunityVO;
import cn.rongcloud.mic.user.model.TUser;
import cn.rongcloud.mic.user.service.UserService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TCommunityUserServiceImpl implements TCommunityUserService {

    @Resource
    private TCommunityUserMapper TCommunityUserMapper;

    @Autowired
    private TCommunityService TCommunityService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private IMSignUtil imSignUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private CommunityMessageUtil communityMessageUtil;

    @Autowired
    private GroupHelper groupHelper;

    @Autowired
    private IMHelper imHelper;

    @Autowired
    private RedisUtils redisUtils;


    @Override
    public RestResult<Integer> update(TCommunityUserUpdateReq params, JwtUser jwtUser) {
        // 参数
        if(StringUtils.isBlank(params.getCommunityUid())||StringUtils.isBlank(params.getUserUid())){
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        // 权限
        TCommunity community = TCommunityService.queryByUid(params.getCommunityUid());
        TCommunityUser communityUser = this.queryByUserIdAndCommunityUid(params.getUserUid(), params.getCommunityUid());
        if(!community.getCreator().equals(jwtUser.getUserId())&&!jwtUser.getUserId().equals(communityUser.getUserUid())){
            return RestResult.generic(RestResultCode.ERR_ACCESS_DENIED);
        }

        TUser userInfo = userService.getUserInfo(params.getUserUid());

        // 清缓存
        String communityUserInfoKey = String.format(CommunityConstant.COMMUNITY_USER_INFO, params.getCommunityUid(),params.getUserUid());
        redisUtils.del(communityUserInfoKey);

        // 更新
        if(StringUtils.isNotBlank(params.getNickName())&&!params.getNickName().equals(communityUser.getNickName())){
            communityUser.setNickName(params.getNickName());
            communityMessageUtil.sendUserUpdateMessage(params.getUserUid(),Lists.newArrayList(params.getCommunityUid()),userInfo.getPortrait(),params.getNickName(),2);
        }

        if(params.getShutUp()!=null&&params.getShutUp().intValue()!=communityUser.getShutUp().intValue()){
            communityUser.setShutUp(params.getShutUp());
            // IM 同步
            IMApiResultInfo resultInfo = null;
            if(params.getShutUp().intValue()==1){
                resultInfo = groupHelper.addGroupBanned(params.getCommunityUid(),Lists.newArrayList(communityUser.getUserUid()));
                String message = String.format("%s 被禁言",communityUser.getNickName());
                //communityMessageUtil.sendCommunityNoticeMessage(jwtUser.getUserId(),Lists.newArrayList(communityUser.getUserUid()),communityUser.getCommunityUid(),message,4);
                communityMessageUtil.sendChannelNoticeMessage(jwtUser.getUserId(),communityUser.getUserUid(),params.getCommunityUid(),community.getMsgChannelUid(),message, ChannelMessageTypeEnum.SHUTUP.getValue());
            }else if(params.getShutUp().intValue()==0){
                resultInfo = groupHelper.delGroupBanned(params.getCommunityUid(), Lists.newArrayList(communityUser.getUserUid()));
                String message = String.format("%s 被解除禁言",communityUser.getNickName());
                //communityMessageUtil.sendCommunityNoticeMessage(jwtUser.getUserId(),Lists.newArrayList(communityUser.getUserUid()),communityUser.getCommunityUid(),message,5);
                communityMessageUtil.sendChannelNoticeMessage(jwtUser.getUserId(),communityUser.getUserUid(),params.getCommunityUid(),community.getMsgChannelUid(),message, ChannelMessageTypeEnum.NOSHUTUP.getValue());
            }
            if(resultInfo==null||!resultInfo.isSuccess()){
                return RestResult.generic(RestResultCode.ERR_OTHER);
            }
        }
        if(params.getFreezeFlag()!=null&& params.getFreezeFlag().intValue()!=communityUser.getFreezeFlag().intValue()){
            communityUser.setFreezeFlag(params.getFreezeFlag());
        }
        if(params.getNoticeType()!=null&& params.getNoticeType().intValue()!=communityUser.getNoticeType().intValue()){
            communityUser.setNoticeType(params.getNoticeType());
        }
        if(params.getStatus()!=null&&params.getStatus().intValue()!=communityUser.getStatus().intValue()){
            communityUser.setStatus(params.getStatus());

            // 审核通过，处理在线列表
            if(params.getStatus().intValue()==2){
                String message = String.format("已被拒绝加入社区【%s】",community.getName());
                communityMessageUtil.sendCommunityNoticeMessage(jwtUser.getUserId(),Lists.newArrayList(communityUser.getUserUid()),communityUser.getCommunityUid(),message,6);
            }else if(params.getStatus().intValue()==3){
                communityUser.setNoticeType(community.getNoticeType());
                // 是否在线
                IMUserStatusResultInfo resultInfo = imHelper.queryUserStatus(communityUser.getUserUid());
                if(resultInfo!=null&&resultInfo.getCode().intValue()==200){
                    String key = null;
                    if(("1").equals(resultInfo.getStatus())){
                        communityUser.setOnlineStatus(1);
                    }else{
                        communityUser.setOnlineStatus(0);
                    }
                }
                groupHelper.joinGroup(communityUser.getCommunityUid(),communityUser.getUserUid());
                // 发送加入社区消息
                String message =  String.format("已加入【%s】社区",community.getName());
                communityMessageUtil.sendCommunityNoticeMessage(jwtUser.getUserId(),Lists.newArrayList(params.getUserUid()),params.getCommunityUid(),message,1);

                communityMessageUtil.sendChannelNoticeMessage(jwtUser.getUserId(),communityUser.getUserUid(),communityUser.getCommunityUid(),community.getMsgChannelUid(),message, ChannelMessageTypeEnum.JOIN.getValue());
            }else if(params.getStatus().intValue()==4||params.getStatus().intValue()==5){
                IMApiResultInfo resultInfo = groupHelper.exitGroup(params.getCommunityUid(), (communityUser.getUserUid()));
                if(resultInfo==null||!resultInfo.isSuccess()){
                    return RestResult.generic(RestResultCode.ERR_OTHER);
                }
                if(params.getStatus().intValue()==4){
                    String message = String.format("已退出【%s】社区",community.getName());
                    communityMessageUtil.sendCommunityNoticeMessage(jwtUser.getUserId(),Lists.newArrayList(params.getUserUid()),params.getCommunityUid(),message,2);
                }else{
                    String message = String.format("已被踢出社区【%s】",community.getName());
                    communityMessageUtil.sendCommunityNoticeMessage(jwtUser.getUserId(),Lists.newArrayList(params.getUserUid()),params.getCommunityUid(),message,3);
                }

            }
        }

        TCommunityUserMapper.updateById(communityUser);
        return RestResult.success();
    }

    /**
     * 在线用户列表
     *
      */

    @Override
    public RestResult<IPage<TCommunityUserResp>> queryPage(TCommunityUserReq params,JwtUser jwtUser) {
        if(params==null||StringUtils.isBlank(params.getCommunityUid())||params.getSelectType()==null){
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        TCommunity tCommunity = TCommunityService.queryByUid(params.getCommunityUid());
        if(tCommunity==null){
            return RestResult.generic(RestResultCode.ERR_COMMUNITY_NOT_EXIT);
        }
        LambdaQueryWrapper<TCommunityUser> queryWrapper = new QueryWrapper<TCommunityUser>().lambda()
                .eq(TCommunityUser::getCommunityUid,params.getCommunityUid()).eq(TCommunityUser::getDelFlag, "0")
                .eq(TCommunityUser::getStatus,CommunityUserStatusEnum.JOIN.getValue());
        if(params.getSelectType().intValue()==0){

        }else if(params.getSelectType().intValue()==1){
            queryWrapper.eq(TCommunityUser::getOnlineStatus,1);
        }else if(params.getSelectType().intValue()==2){
            queryWrapper.eq(TCommunityUser::getOnlineStatus,0);
        }else if(params.getSelectType().intValue()==3){
            queryWrapper.eq(TCommunityUser::getFreezeFlag,1);
        }else if(params.getSelectType().intValue()==4){
            queryWrapper.eq(TCommunityUser::getShutUp,1);
        }
        if(StringUtils.isNotBlank(params.getNickName())){
            queryWrapper.like(TCommunityUser::getNickName,params.getNickName());
        }
        Page<TCommunityUser> pageParam = new Page<>();
        pageParam.setCurrent(params.getPageNum());
        pageParam.setSize(params.getPageSize());
        IPage pageResult = TCommunityUserMapper.selectPage(pageParam, queryWrapper);
        List<TCommunityUser> records = pageResult.getRecords();
        Set<String> userids = records.stream().map(item -> item.getUserUid()).collect(Collectors.toSet());
        List<TCommunityUserResp> userResps = warpCommunityUserResp(userids, tCommunity.getCreator(),records);
        pageResult.setRecords(userResps);
        return RestResult.success(pageResult);
    }

    private List<TCommunityUserResp> warpCommunityUserResp(Set<String> userIds,String creator,List<TCommunityUser> records){
        Map<String, TCommunityUser> userIdMap = records.stream().collect(Collectors.toMap(TCommunityUser::getUserUid, o -> o, (k1, k2) -> k1));
        List<TCommunityUserResp> userResps = Lists.newArrayList();
        for(String userId :userIds){
            boolean creatorFlag = false;
            if(creator.equals(userId)){
                creatorFlag = true;
            }
            TUser userInfo = userService.getUserInfo(userId);
            TCommunityUser communityUser = userIdMap.get(userId);
            TCommunityUserResp userResp = TCommunityUserResp.builder().userUid(userId).name(communityUser==null?"":communityUser.getNickName())
                    .shutUp(communityUser==null?0:communityUser.getShutUp())
                    .onlineStatus(communityUser==null?0:communityUser.getOnlineStatus())
                    .portrait(userInfo.getPortrait()).creatorFlag(creatorFlag).build();
            userResps.add(userResp);
        }
        return userResps;
    }

    @Override
    public RestResult<IPage<TCommunityVO>> pageCommunity(PageParam params, JwtUser jwtUser) {
        Page<TCommunityUser> pageParam = new Page<>();
        pageParam.setCurrent(params.getPageNum());
        pageParam.setSize(params.getPageSize());
        LambdaQueryWrapper<TCommunityUser> queryWrapper = new QueryWrapper<TCommunityUser>().lambda()
                .eq(TCommunityUser::getStatus,CommunityUserStatusEnum.JOIN.getValue())
                .eq(TCommunityUser::getUserUid, jwtUser.getUserId())
                .eq(TCommunityUser::getDelFlag, "0")
                .orderByDesc(TCommunityUser::getId);
        IPage pageResult = TCommunityUserMapper.selectPage(pageParam,queryWrapper);
        List<TCommunityUser> records = pageResult.getRecords();
        if(CollectionUtils.isEmpty(records)){
            return RestResult.success(pageResult);
        }
        List<String> communityUids = records.stream().map(item -> item.getCommunityUid()).collect(Collectors.toList());
        List<TCommunityVO> pageVOS = Lists.newArrayList();
        for(String communityUid:communityUids){
            TCommunityVO vo = new TCommunityVO();
            TCommunity community = TCommunityService.queryByUid(communityUid);
            BeanUtils.copyProperties(community,vo);
            vo.setCommunityUid(community.getUid());
            pageVOS.add(vo);
        }
        pageResult.setRecords(pageVOS);
        return RestResult.success(pageResult);
    }

    @Override
    public RestResult<Integer> join(String communityUid, JwtUser jwtUser) {
        // 如果社区
        TCommunity community = TCommunityService.queryByUid(communityUid);
        if(community==null){
            return RestResult.generic(RestResultCode.ERR_COMMUNITY_NOT_EXIT);
        }
        TCommunityUser communityUser = this.queryByUserIdAndCommunityUid(jwtUser.getUserId(), communityUid);
        if(communityUser!=null&&(communityUser.getStatus().intValue()==CommunityUserStatusEnum.AUDITING.getValue()||communityUser.getStatus().intValue()==CommunityUserStatusEnum.JOIN.getValue())){
            return RestResult.generic(RestResultCode.ERR_HAVE_JOIN);
        }
        int needAudit = NeedAuditEnum.YES.getValue();
        if(community.getCreator().equals(jwtUser.getUserId())||community.getNeedAudit().intValue()== NeedAuditEnum.NO.getValue()){
            needAudit=NeedAuditEnum.NO.getValue();
        }

        // 清缓存
        String communityUserInfoKey = String.format(CommunityConstant.COMMUNITY_USER_INFO, communityUid,jwtUser.getUserId());
        redisUtils.del(communityUserInfoKey);

        // 是否在线
        Integer onlineStatus = 0;
        IMUserStatusResultInfo resultInfo = imHelper.queryUserStatus(jwtUser.getUserId());
        if(resultInfo!=null&&resultInfo.getCode().intValue()==200) {
            if (("1").equals(resultInfo.getStatus())) {
                onlineStatus = 1;
            } else {
                onlineStatus = 0;
            }
        }
        if(communityUser!=null){
            log.info("user new join :{}",communityUser);
            communityUser.setOnlineStatus(onlineStatus);
            communityUser.setStatus(needAudit==NeedAuditEnum.YES.getValue()? CommunityUserStatusEnum.AUDITING.getValue():CommunityUserStatusEnum.JOIN.getValue());
            TCommunityUserMapper.updateById(communityUser);
        }else{

            communityUser = TCommunityUser.builder().communityUid(communityUid).userUid(jwtUser.getUserId())
                    .nickName(jwtUser.getUserName())
                    .onlineStatus(onlineStatus)
                    .noticeType(community.getNoticeType())
                    .status(needAudit==NeedAuditEnum.YES.getValue()? CommunityUserStatusEnum.AUDITING.getValue():CommunityUserStatusEnum.JOIN.getValue())
                    .build();
            TCommunityUserMapper.insert(communityUser);
        }

        if(needAudit==NeedAuditEnum.YES.getValue()){
            // 发送审批消息
            String message = String.format("申请加入社区【%s】",community.getName());
            communityMessageUtil.sendCommunityNoticeMessage(jwtUser.getUserId(),Lists.newArrayList(community.getCreator()),communityUser.getCommunityUid(),message,0);
        }else{
            groupHelper.joinGroup(communityUser.getCommunityUid(),jwtUser.getUserId());
            // 发送加入社区消息
            if(!community.getCreator().equals(jwtUser.getUserId())){
                String message =  String.format("已加入【%s】社区",community.getName());
                communityMessageUtil.sendCommunityNoticeMessage(jwtUser.getUserId(),Lists.newArrayList(jwtUser.getUserId()),communityUser.getCommunityUid(),message,1);
                String channelMessage =  String.format("欢迎 %s 加入社区",jwtUser.getUserName());
                communityMessageUtil.sendChannelNoticeMessage(jwtUser.getUserId(),communityUser.getUserUid(),communityUser.getCommunityUid(),community.getMsgChannelUid(),channelMessage, ChannelMessageTypeEnum.JOIN.getValue());
            }
        }
        return RestResult.success(communityUser.getStatus());
    }

    @Override
    public void deleteByCommunityUid(String communityUid) {
        TCommunityUser communityUser = TCommunityUser.builder().delFlag(1).build();
        TCommunityUserMapper.update(communityUser,new QueryWrapper<TCommunityUser>().lambda().eq(TCommunityUser::getCommunityUid,communityUid));
    }

    @Override
    public RestResult<Integer> updateChannel(TChannelUserUpdateReq params, JwtUser jwtUser) {
        TCommunityUser communityUser = queryByUserIdAndCommunityUid(jwtUser.getUserId(), params.getCommunityUid());
        if(communityUser==null){
            return RestResult.generic(RestResultCode.ERR_COMMUNITY_NOT_EXIT);
        }
        // 清缓存
        String communityUserInfoKey = String.format(CommunityConstant.COMMUNITY_USER_INFO, params.getCommunityUid(),jwtUser.getUserId());
        redisUtils.del(communityUserInfoKey);

        List<TUserChannelSettingDTO> dtos = Lists.newArrayList();
        String channelSetting = communityUser.getChannelSetting();
        if(StringUtils.isNotBlank(channelSetting)){
            dtos = JSON.parseArray(channelSetting, TUserChannelSettingDTO.class);
            //dtos = (List<TUserChannelSettingDTO>)GsonUtil.fromJson(channelSetting, ArrayList.class);
            List<String> channelUids = dtos.stream().map(item -> item.getChannelUid()).collect(Collectors.toList());
            if(channelUids.contains(params.getChannelUid())){
                dtos.stream().forEach(item->{
                    if(item.getChannelUid().equals(params.getChannelUid())){
                        item.setNoticeType(params.getNoticeType());
                    }
                });
            }else{
                TUserChannelSettingDTO dto = TUserChannelSettingDTO.builder().channelUid(params.getChannelUid()).noticeType(params.getNoticeType()).build();
                dtos.add(dto);
            }

        }else{
            TUserChannelSettingDTO dto = TUserChannelSettingDTO.builder().channelUid(params.getChannelUid()).noticeType(params.getNoticeType()).build();
            dtos.add(dto);
        }
        communityUser.setChannelSetting(GsonUtil.toJson(dtos));
        TCommunityUserMapper.updateById(communityUser);
        return RestResult.success();
    }

    @Override
    public List<TCommunityUser> queryByUserIdsAndCommunityUid(Set<String> userIds, String communityUid) {
        LambdaQueryWrapper<TCommunityUser> queryWrapper = new QueryWrapper<TCommunityUser>().lambda()
                .eq(TCommunityUser::getCommunityUid, communityUid)
                .in(TCommunityUser::getUserUid, userIds)
                .eq(TCommunityUser::getDelFlag, 0);
        return TCommunityUserMapper.selectList(queryWrapper);
    }


    @Override
    public TCommunityUser queryByUserIdAndCommunityUid(String userId, String communityUid) {
        // 缓存拿
        String communityUserInfoKey = String.format(CommunityConstant.COMMUNITY_USER_INFO, communityUid,userId);
        TCommunityUser communityUser = (TCommunityUser)redisUtils.get(communityUserInfoKey);
        if(communityUser!=null){
            return communityUser;
        }
        LambdaQueryWrapper<TCommunityUser> queryWrapper = new QueryWrapper<TCommunityUser>().lambda().eq(TCommunityUser::getCommunityUid, communityUid)
                .eq(TCommunityUser::getUserUid, userId).eq(TCommunityUser::getDelFlag, 0);
        communityUser = TCommunityUserMapper.selectOne(queryWrapper);
        if(communityUser!=null){
            redisUtils.set(communityUserInfoKey,communityUser,CommunityConstant.INFO_TIME);
        }
        return communityUser;
    }

    @Override
    public RestResult statusSync(Long timestamp, String nonce, String signature, List<UserStatusSyncDTO> dtos) {
        //校验签名是否正确
        if (!imSignUtil.validSign(nonce, timestamp.toString(), signature)) {
            log.info("user status sync Access denied: signature error, signTimestamp:{}, nonce:{}, signature:{}", timestamp, nonce, signature);
            return RestResult.success();
        }

        for(UserStatusSyncDTO dto:dtos){
            log.info("user status sync dto:{}", GsonUtil.toJson(dto));
            String userUid = dto.getUserid();
            // 这里最好用布隆过滤器先过滤一下
            //LambdaQueryWrapper<TCommunityUser> queryWrapper = new QueryWrapper<TCommunityUser>().lambda().eq(TCommunityUser::getUserUid, userUid).eq(TCommunityUser::getDelFlag, "0");
            //List<TCommunityUser> communityUsers = TCommunityUserMapper.selectList(queryWrapper);
            Integer onlineStatus = Integer.parseInt(dto.getStatus())==0?1:0;
            TCommunityUserMapper.updateOnlineStatusByUserId(dto.getUserid(),onlineStatus);
        }
        return RestResult.generic(200,"success");
    }

    @Override
    public List<CommunityUserCount> queryUserCountByCommunityUids(List<String> communityUids) {
        if(CollectionUtils.isEmpty(communityUids)){
            return Lists.newArrayList();
        }
        return TCommunityUserMapper.queryUserCountByCommunityUids(communityUids);
    }

    @Override
    public List<TCommunityUser> queryByUserId(String userId) {
        LambdaQueryWrapper<TCommunityUser> queryWrapper = new QueryWrapper<TCommunityUser>().lambda().eq(TCommunityUser::getUserUid, userId).eq(TCommunityUser::getDelFlag, "0");
        return TCommunityUserMapper.selectList(queryWrapper);
    }

    @Override
    public RestResult<TCommunityUserInfoResp> userInfo(TCommunityUserInfoReq params, JwtUser jwtUser) {
        if(params==null||StringUtils.isBlank(params.getCommunityUid())||StringUtils.isBlank(params.getUserUid())){
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        TCommunityUser tCommunityUser = queryByUserIdAndCommunityUid(params.getUserUid(), params.getCommunityUid());
        if(tCommunityUser==null){
            return RestResult.generic(RestResultCode.ERR_NOT_FIND);
        }

        TUser userInfo = userService.getUserInfo(params.getUserUid());
        if(userInfo==null){
            return RestResult.generic(RestResultCode.ERR_ROOM_USER_IS_NOT_EXIST);
        }

        TCommunityUserInfoResp userInfoResp = TCommunityUserInfoResp.builder().userUid(params.getUserUid())
                .status(tCommunityUser.getStatus())
                .nickName(tCommunityUser.getNickName()).portrait(userInfo.getPortrait()).build();
        return RestResult.success(userInfoResp);
    }

    @Override
    public int updateJoinByCommunityUid(String uid) {
        if(StringUtils.isBlank(uid)){
            return 0;
        }
        TCommunityUser communityUser = new TCommunityUser();
        communityUser.setStatus(CommunityUserStatusEnum.JOIN.getValue());

        LambdaQueryWrapper<TCommunityUser> queryWrapper = new QueryWrapper<TCommunityUser>().lambda()
                .eq(TCommunityUser::getCommunityUid, uid).eq(TCommunityUser::getStatus,CommunityUserStatusEnum.AUDITING.getValue())
                .eq(TCommunityUser::getDelFlag, "0");

        List<TCommunityUser> communityUsers = TCommunityUserMapper.selectList(queryWrapper);
        for(TCommunityUser tCommunityUser:communityUsers){
            // 清缓存
            String communityUserInfoKey = String.format(CommunityConstant.COMMUNITY_USER_INFO, tCommunityUser.getCommunityUid(),tCommunityUser.getUserUid());
            redisUtils.del(communityUserInfoKey);

            TCommunity tCommunity = TCommunityService.queryByUid(tCommunityUser.getCommunityUid());
            groupHelper.joinGroup(tCommunityUser.getCommunityUid(),tCommunityUser.getUserUid());
            // 发送加入社区消息
            String message =  String.format("已加入「%s」社区",tCommunity.getName());
            //communityMessageUtil.sendJoinCommunityMessage(jwtUser.getUserId(),communityUser.getCommunityUid(),community.getName());
            communityMessageUtil.sendCommunityNoticeMessage(tCommunityUser.getUserUid(),Lists.newArrayList(tCommunityUser.getUserUid()),tCommunityUser.getCommunityUid(),message,1);
            String channelMessage =  String.format("欢迎 %s 加入社区",tCommunityUser.getNickName());
            communityMessageUtil.sendChannelNoticeMessage(tCommunityUser.getUserUid(),tCommunityUser.getUserUid(),tCommunityUser.getCommunityUid(),tCommunity.getMsgChannelUid(),channelMessage, ChannelMessageTypeEnum.JOIN.getValue());
        }
        TCommunityUser communityUser2 = new TCommunityUser();
        communityUser2.setStatus(0);
        LambdaQueryWrapper<TCommunityUser> queryWrapper2 = new QueryWrapper<TCommunityUser>().lambda()
                .eq(TCommunityUser::getCommunityUid, uid).eq(TCommunityUser::getStatus,CommunityUserStatusEnum.REFUSE.getValue())
                .eq(TCommunityUser::getDelFlag, "0");
        List<TCommunityUser> communityUsers2 = TCommunityUserMapper.selectList(queryWrapper2);
        for(TCommunityUser tCommunityUser:communityUsers2) {
            // 清缓存
            String communityUserInfoKey = String.format(CommunityConstant.COMMUNITY_USER_INFO, tCommunityUser.getCommunityUid(), tCommunityUser.getUserUid());
            redisUtils.del(communityUserInfoKey);
        }
        TCommunityUserMapper.update(communityUser2,queryWrapper2);
        return TCommunityUserMapper.update(communityUser,queryWrapper);
    }

    @Override
    public List<TCommunityUser> queryByCommunityUid(String uid) {
        if(StringUtils.isBlank(uid)){
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<TCommunityUser> queryWrapper = new QueryWrapper<TCommunityUser>().lambda().eq(TCommunityUser::getDelFlag, 0)
                .eq(TCommunityUser::getCommunityUid, uid).in(TCommunityUser::getStatus, Lists.newArrayList(1, 2, 3));
        return TCommunityUserMapper.selectList(queryWrapper);
    }

}