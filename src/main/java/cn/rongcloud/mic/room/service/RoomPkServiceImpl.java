package cn.rongcloud.mic.room.service;

import cn.rongcloud.common.im.IMHelper;
import cn.rongcloud.common.im.pojos.IMApiResultInfo;
import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.common.utils.IdentifierUtils;
import cn.rongcloud.mic.common.constant.CustomerConstant;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.redis.RedisUtils;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.rest.RestResultCode;
import cn.rongcloud.mic.room.constant.RoomKeysCons;
import cn.rongcloud.mic.room.dao.RoomDao;
import cn.rongcloud.mic.room.enums.ChrmRoomPkStatus;
import cn.rongcloud.mic.room.key.KeyGen;
import cn.rongcloud.mic.room.message.ChrmPkMessage;
import cn.rongcloud.mic.room.message.ChrmPkStatusMessage;
import cn.rongcloud.mic.room.model.TRoom;
import cn.rongcloud.mic.room.pojos.ReqRoomPk;
import cn.rongcloud.mic.room.pojos.ResRoomPkInfo;
import cn.rongcloud.mic.room.pojos.ResRoomPkScore;
import cn.rongcloud.mic.room.pojos.ResRoomScore;
import cn.rongcloud.mic.room.pojos.RespRoomUser;
import cn.rongcloud.mic.room.pojos.RoomPKInfo;
import cn.rongcloud.mic.user.model.TUser;
import cn.rongcloud.mic.user.service.UserService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class RoomPkServiceImpl implements RoomPkService {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private UserService userService;

    @Autowired
    IMHelper imHelper;


    // 3分钟
    private static final Integer pkTime = 1000*60*3;

    private String getRedisRoomPkGiftKey(String roomId) {
        return KeyGen.getRedisRoomPkGiftKey(roomId);
    }

    @Override
    public RestResult pk(ReqRoomPk data, JwtUser jwtUser) {
        // 0 开始 1 暂停，惩罚阶段（送礼物不计算了） 2 结束
        // flag 为true 代表PK 还未结束
        boolean flag = redisUtils.hHasKey(RoomKeysCons.REDIS_ROOM_PK_ROOM_KEY, data.getToRoomId())
                || redisUtils.hHasKey(RoomKeysCons.REDIS_ROOM_PK_ROOM_KEY, data.getRoomId());
        if (flag && data.getStatus().equals(ChrmRoomPkStatus.START.getValue())) {
            return RestResult.generic(RestResultCode.ERR_ROOM_IS_PK);
        }
        if (data.getStatus().equals(ChrmRoomPkStatus.STOP.getValue())&&flag) {
            // 只发送停止消息  主动停止pkId 为null
            CompletableFuture.runAsync(()->{
                this.sendStopMessage(data.getRoomId(),data.getToRoomId(),null);
            });
        } else if(data.getStatus().equals(ChrmRoomPkStatus.START.getValue())) {
            // pk 的双方 只会有一方调用pk 接口
            TRoom otherRoom = roomDao.findTRoomByUidEquals(data.getToRoomId());
            // roomId、status、date
            String pkId = IdentifierUtils.uuid36();
            RoomPKInfo pkInfo = new RoomPKInfo();
            pkInfo.setPkId(pkId);
            pkInfo.setStatus(data.getStatus());
            pkInfo.setTime(System.currentTimeMillis());

            // 不同的房间信息不同
            pkInfo.setUserId(otherRoom.getUserId());
            pkInfo.setToRoomId(data.getRoomId());
            redisUtils.hset(RoomKeysCons.REDIS_ROOM_PK_ROOM_KEY, data.getToRoomId(), GsonUtil.toJson(pkInfo));

            // 发起方
            pkInfo.setToRoomId(data.getToRoomId());
            pkInfo.setUserId(jwtUser.getUserId());
            pkInfo.setLeader(true);
            redisUtils.hset(RoomKeysCons.REDIS_ROOM_PK_ROOM_KEY, data.getRoomId(), GsonUtil.toJson(pkInfo));

            // 开启发送消息任务
            CompletableFuture.runAsync(()->{
                this.startPkMessageTask(data.getRoomId(),data.getToRoomId(),pkId);
            });
        }
        return RestResult.success();
    }

    @Override
    public RestResult<ResRoomPkInfo> pkInfo(String roomId) {
        ResRoomPkInfo pkInfo = getPkRoomDetail(roomId);
        return RestResult.success(pkInfo);
    }

    @Override
    public RestResult<Boolean> isPK(String roomId) {
        RoomPKInfo pkInfo = this.getPkInfo(roomId);
        return RestResult.success(pkInfo == null ? false : true);
    }

    @Override
    public RestResult<ResRoomPkScore> getPkDetail(String roomId) {
        ResRoomPkScore pkScore = getPkRoomInfo(roomId);
        return RestResult.success(pkScore);
    }

    /**
     * 获取pk单个房间信息
     * @param roomId
     * @return
     */
    private ResRoomPkInfo getPkRoomDetail(String roomId){
        Double total = redisUtils.score(RoomKeysCons.REDIS_ROOM_PK_SCORE_KEY, roomId);
        ResRoomPkInfo pkInfo = new ResRoomPkInfo();
        pkInfo.setScore(total == null ? 0L : total.longValue());
        List<RespRoomUser> roomUser = getRoomUser(roomId);
        pkInfo.setUserInfoList(roomUser);
        RoomPKInfo roomPKInfo = this.getPkInfo(roomId);
        if (roomPKInfo != null) {
            pkInfo.setPkTime(roomPKInfo.getTime());
            pkInfo.setUserId(roomPKInfo.getUserId());
            pkInfo.setLeader(roomPKInfo.isLeader());

        }
        return pkInfo;
    }

    /**
     * 获取房间中送礼物前三名用户
     * @param roomId
     * @return
     */
    private List<RespRoomUser> getRoomUser(String roomId){
        LinkedHashSet range = (LinkedHashSet) redisUtils.reverseRange(getRedisRoomPkGiftKey(roomId), 0L, 2L);
        List<RespRoomUser> list = new ArrayList<>();
        range.forEach(userId -> {
            TUser userInfo = userService.getUserInfo((String) userId);
            if (userInfo != null) {
                RespRoomUser roomUser = new RespRoomUser();
                roomUser.setPortrait(userInfo.getPortrait());
                roomUser.setUserId(userInfo.getUid());
                roomUser.setUserName(userInfo.getName());
                list.add(roomUser);
            }
        });
        return list;
    }

    /**
     * 获取某个房间的PK 信息
     *
     * @param roomId
     * @return
     */
    public RoomPKInfo getPkInfo(String roomId) {
        Object pkInfo = redisUtils.hget(RoomKeysCons.REDIS_ROOM_PK_ROOM_KEY, roomId);
        if (pkInfo != null) {
            return (RoomPKInfo) GsonUtil.fromJson(pkInfo.toString(), RoomPKInfo.class);
        }
        return null;
    }

    /**
     * 获取Pk 双方的房间信息
     * @param roomId
     * @return
     */
    private ResRoomPkScore getPkRoomInfo(String roomId){
        ResRoomPkScore pkScore = new ResRoomPkScore();
        RoomPKInfo pkInfo = getPkInfo(roomId);
        if(pkInfo==null){
            pkScore.setStatusMsg(ChrmRoomPkStatus.STOP.getValue());
            return pkScore;
        }
        pkScore.setStatusMsg(pkInfo.getStatus());
        Long time = pkInfo.getTime();
        // 获取当前时间和pk时间 的差值
        Long timeDiff = System.currentTimeMillis()-time;
        if(ChrmRoomPkStatus.PUNISH.getValue()==pkInfo.getStatus().intValue()){
            timeDiff = timeDiff-pkTime;
        }
        pkScore.setTimeDiff(timeDiff);

        ResRoomPkInfo pkRoomDetail = getPkRoomDetail(roomId);
        List<ResRoomScore> resRoomScores = Lists.newArrayList();
        ResRoomScore roomScore = new ResRoomScore();
        roomScore.setRoomId(roomId);
        roomScore.setUserId(pkRoomDetail.getUserId());
        roomScore.setScore(pkRoomDetail.getScore());
        roomScore.setUserInfoList(pkRoomDetail.getUserInfoList());
        roomScore.setLeader(pkRoomDetail.isLeader());
        resRoomScores.add(roomScore);

        // 对方房间的积分值
        ResRoomPkInfo otherRoom = getPkRoomDetail(pkInfo.getToRoomId());
        ResRoomScore otherRoomScore = new ResRoomScore();
        otherRoomScore.setRoomId(pkInfo.getToRoomId());
        otherRoomScore.setUserId(otherRoom.getUserId());
        otherRoomScore.setScore(otherRoom.getScore());
        otherRoomScore.setUserInfoList(otherRoom.getUserInfoList());
        otherRoomScore.setLeader(otherRoom.isLeader());
        resRoomScores.add(otherRoomScore);
        pkScore.setRoomScores(resRoomScores);
        return pkScore;
    }

    /**
     * 发送开始PK 消息
     * @param roomId
     * @param toRoomId
     */
    private void sendStartMessage(String roomId,String toRoomId){
        ResRoomPkScore pkRoomInfo = getPkRoomInfo(roomId);
        // 发送开始pk 消息
        ChrmPkStatusMessage message = ChrmPkStatusMessage.builder().statusMsg(ChrmRoomPkStatus.START.getValue())
                .stopPkRoomId("").timeDiff(0L).roomScores(pkRoomInfo.getRoomScores())
                .build();
        String[] sendRoomIdList = {roomId, toRoomId};
        try {
            log.info("send pk message start status roomId:{},toRoomId:{}",roomId,toRoomId);
            imHelper.publishMessage(CustomerConstant.SYSTEM_UID, sendRoomIdList, message);
        } catch (Exception e) {
            log.error("sendStartMessage error roomId:{},toRoomId:{},message:{}",roomId,toRoomId,e.getMessage(),e);
        }
    }

    /**
     * 发送惩罚消息
     * @param roomId
     * @param toRoomId
     */
    private void sendPunishMessage(String roomId,String toRoomId,String pkId){
        // pk 如果结束，直接返回
        RoomPKInfo pkInfo = getPkInfo(roomId);
        // 如果pkInfo 为空说明roomId 没有在pk,不需要发送，如果不为空，那么可能是进行了下一场pk
        if(pkInfo==null||(StringUtils.isNotBlank(pkId)&&!pkInfo.getPkId().equals(pkId))){
            return;
        }

        RoomPKInfo toPkInfo = getPkInfo(toRoomId);
        // 如果pkInfo 为空说明roomId 没有在pk,不需要发送，如果不为空，那么可能是进行了下一场pk
        if(toPkInfo==null||(StringUtils.isNotBlank(pkId)&&!toPkInfo.getPkId().equals(pkId))){
            return;
        }
        // 将 pk 状态重装进缓存
        pkInfo.setStatus(ChrmRoomPkStatus.PUNISH.getValue());
        redisUtils.hset(RoomKeysCons.REDIS_ROOM_PK_ROOM_KEY, roomId, GsonUtil.toJson(pkInfo));
        toPkInfo.setStatus(ChrmRoomPkStatus.PUNISH.getValue());
        redisUtils.hset(RoomKeysCons.REDIS_ROOM_PK_ROOM_KEY, toRoomId, GsonUtil.toJson(toPkInfo));

        // 发送开始pk 消息 要把响应的得分带过去
        ResRoomPkScore pkRoomInfo = getPkRoomInfo(roomId);
        ChrmPkStatusMessage message = ChrmPkStatusMessage.builder().statusMsg(ChrmRoomPkStatus.PUNISH.getValue())
                .timeDiff(0L).roomScores(pkRoomInfo.getRoomScores()).build();
        String[] sendRoomIdList = {roomId, toRoomId};
        try {
            log.info("send pk message punish status roomId:{},toRoomId:{}",roomId,toRoomId);
            imHelper.publishMessage(CustomerConstant.SYSTEM_UID, sendRoomIdList, message);
        } catch (Exception e) {
            log.info("sendPunishMessage error roomId:{},toRoomId:{},message:{}",roomId,toRoomId,e.getMessage(),e);
        }
    }

    /**
     * 发送停止消息
     * @param roomId
     * @param toRoomId
     */
    private void sendStopMessage(String roomId,String toRoomId,String pkId){
        // pk 如果结束，直接返回
        RoomPKInfo pkInfo = getPkInfo(roomId);
        // 如果pkInfo 为空说明roomId 没有在pk,不需要发送，如果不为空，那么可能是进行了下一场pk
        if(pkInfo==null||(StringUtils.isNotBlank(pkId)&&!pkInfo.getPkId().equals(pkId))){
            return;
        }
        // 停止消息分两种，1.惩罚阶段后自动停止  2. 房主主动停止
        // 发送开始pk 消息  要把谁停止的带过去
        ResRoomPkScore pkRoomInfo = getPkRoomInfo(roomId);
        // PK 双方都调用PK 结束的时候，会有并发问题，用这个解决
        if(pkRoomInfo.getTimeDiff()==null){
            return;
        }
        ChrmPkStatusMessage message = ChrmPkStatusMessage.builder().statusMsg(ChrmRoomPkStatus.STOP.getValue())
                .timeDiff(pkRoomInfo.getTimeDiff()).roomScores(pkRoomInfo.getRoomScores())
                .stopPkRoomId(StringUtils.isBlank(pkId)?roomId:"").build();

        String[] sendRoomIdList = {roomId, toRoomId};
        try {
            // 发送消息
            log.info("send pk message stop status roomId:{},toRoomId:{},pkId:{}",roomId,toRoomId,pkId);
            imHelper.publishMessage(CustomerConstant.SYSTEM_UID, sendRoomIdList, message);

            // pk 结束清缓存
            cleanCacheByStopPk(roomId);

        } catch (Exception e) {
            log.info("sendStopMessage error roomId:{},toRoomId:{},message:{}",roomId,toRoomId,e.getMessage(),e);
        }

    }

    /**
     *  PK 停止时清空PK 的相关信息
     * @param roomId
     */
    @Override
    public void cleanCacheByStopPk(String roomId){
        RoomPKInfo pkInfo = getPkInfo(roomId);
        if(pkInfo==null){
            return;
        }
        redisUtils.hdel(RoomKeysCons.REDIS_ROOM_PK_ROOM_KEY, pkInfo.getToRoomId());
        redisUtils.hdel(RoomKeysCons.REDIS_ROOM_PK_ROOM_KEY, roomId);
        redisUtils.del(getRedisRoomPkGiftKey(roomId), getRedisRoomPkGiftKey(pkInfo.getToRoomId()));
        redisUtils.zremove(RoomKeysCons.REDIS_ROOM_PK_SCORE_KEY, roomId);
        redisUtils.zremove(RoomKeysCons.REDIS_ROOM_PK_SCORE_KEY, pkInfo.getToRoomId());
    }

    public RestResult startPkMessageTask(String roomId,String toRoomId,String pkId){
        try {
            // 1.发送开始pk 消息
            sendStartMessage(roomId,toRoomId);

            //2. pk开始后，睡眠3分钟后发送惩罚消息
            Thread.sleep(pkTime);
            sendPunishMessage(roomId,toRoomId,pkId);

            //3. 惩罚阶段为3分钟，惩罚结束发送结束消息
            Thread.sleep(pkTime);
            sendStopMessage(roomId,toRoomId,pkId);
        } catch (InterruptedException e) {
            log.info("PK message status task stop roomId:{},toRoomId:{}",roomId,toRoomId);
        }
        return RestResult.success("ok");
    }

    /**
     * 赠送礼物 验证在pk 过程中需要 记录pk 房间分值 和 pk 分值最高三人列表
     * 最后发送两个聊天室的广播消息
     *
     * @param roomId
     * @param formUid  发送礼物的人
     * @param currentValue 发送的礼物的价值
     */
    @Async
    @Override
    public void pkGift(String roomId, String formUid,String toUid, Long currentValue) {
        RoomPKInfo pkInfo = this.getPkInfo(roomId);
        log.info("pk gift send", pkInfo);
        if (pkInfo != null && pkInfo.getStatus() != null && pkInfo.getStatus().equals(0)&&pkInfo.getUserId().equals(toUid)) {
            //给当前的用户在该roomId赠送礼物价值基础上，再增加上本次赠送的礼物价值currentValue
            redisUtils.incrementScore(getRedisRoomPkGiftKey(roomId), formUid, currentValue);
            //房间pk总分钟数也要重置
            redisUtils.incrementScore(RoomKeysCons.REDIS_ROOM_PK_SCORE_KEY, roomId, currentValue);
            //广播消息给两个房主
            Double total = redisUtils.score(RoomKeysCons.REDIS_ROOM_PK_SCORE_KEY, roomId);// 获取元素的分值
            ChrmPkMessage pkMessage = new ChrmPkMessage();
            pkMessage.setRoomId(roomId);
            pkMessage.setScore(total.longValue());
            List<RespRoomUser> roomUser = getRoomUser(roomId);
            pkMessage.setUserList(roomUser);
            pkMessage.setPkTime(pkInfo.getTime());
            String[] sendRoomIdList = {roomId, pkInfo.getToRoomId()};
            try {
                IMApiResultInfo resultInfo = imHelper.publishMessage(CustomerConstant.SYSTEM_UID, sendRoomIdList, pkMessage);
                log.info("pk gift send result:{}",resultInfo);
            } catch (Exception e) {
                log.info("pk gift error {}", e);
            }

            // new app version need
            ResRoomPkScore pkScore = getPkRoomInfo(roomId);
            try {
                IMApiResultInfo resultInfo = imHelper.publishMessage(CustomerConstant.SYSTEM_UID, sendRoomIdList, pkScore);
                log.info("new pk gift send result:{}",resultInfo);
            } catch (Exception e) {
                log.info("pk gift error {}", e);
            }
        }
    }

}
