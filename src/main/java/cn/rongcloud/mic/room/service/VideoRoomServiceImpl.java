package cn.rongcloud.mic.room.service;

import cn.rongcloud.common.im.ChrmEntrySetInfo;
import cn.rongcloud.common.im.IMHelper;
import cn.rongcloud.common.im.RTCHelper;
import cn.rongcloud.common.im.pojos.IMApiResultInfo;
import cn.rongcloud.common.im.pojos.ImChatroomEntryResultInfo;
import cn.rongcloud.common.im.pojos.RTCRoomMemberResp;
import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.room.enums.RoomTypeEnum;
import cn.rongcloud.mic.room.message.ChrmSeatRemoveMessage;
import cn.rongcloud.mic.room.model.TRoom;
import cn.rongcloud.mic.room.pojos.ReqRoomMusicDelete;
import cn.rongcloud.mic.room.pojos.ReqVideoRoomStatusSync;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VideoRoomServiceImpl implements VideoRoomService{

    @Autowired
    IMHelper imHelper;

    @Autowired
    RTCHelper rtcHelper;

    @Autowired
    RoomService roomService;

    @Autowired
    RoomMusicService roomMusicService;

    @Autowired
    RoomPkService roomPkService;

    @Override
    public RestResult statusSync(ReqVideoRoomStatusSync statusSync) {
        if(statusSync==null||statusSync.getEvent().intValue()!=12){
            return RestResult.success();
        }
        if(statusSync.getExitType().intValue()!=3){
            return RestResult.success();
        }
        try {

            // music kit need
            ChrmSeatRemoveMessage message = new ChrmSeatRemoveMessage();
            message.setRoomId(statusSync.getRoomId());
            message.setUserId(statusSync.getUserId());
            message.setTimestamp(System.currentTimeMillis());
            String uid = "_SYSTEM_";
            // 删除正在播放的音乐
            ReqRoomMusicDelete data = new ReqRoomMusicDelete();
            data.setRoomId(statusSync.getRoomId());
            roomMusicService.musicPlay(data);
            // 发送聊天室消息
            imHelper.publishMessage(uid,statusSync.getRoomId(),message);

            // pk need  清pk
            roomPkService.cleanCacheByStopPk(statusSync.getRoomId());


            // 防止异常退出回调到服务端之前，又重新进入房间
            RTCRoomMemberResp rtcResp = rtcHelper.queryRoomMembers(statusSync.getRoomId());
            log.info("exception out roomId:{} statusSync:{},rtcResp:{}",statusSync.getRoomId(),GsonUtil.toJson(statusSync),GsonUtil.toJson(rtcResp));
            if(rtcResp!=null&&rtcResp.getCode()==200){
                List<String> userIds = rtcResp.getMembers().stream().map(RTCRoomMemberResp.Members::getUserId).collect(Collectors.toList());
                if(!CollectionUtils.isEmpty(userIds)&&userIds.contains(statusSync.getUserId())){
                    return RestResult.success();
                }
            }

            ImChatroomEntryResultInfo resultInfo = imHelper.entrySetQuery(statusSync.getRoomId());
            if(resultInfo==null|| CollectionUtils.isEmpty(resultInfo.getKeys())){
                log.info("room entrySet is null roomId:{}",statusSync.getRoomId());
                return RestResult.success();
            }

            // 这里需要区分一下房间类型
            TRoom room = roomService.getRoomInfo(statusSync.getRoomId());
            log.info("exception out room:{},resultInfo:{}",GsonUtil.toJson(room),GsonUtil.toJson(resultInfo));
            if(room==null){
                return RestResult.success();
            }
            ChrmEntrySetInfo newSetInfo =null;
            if(RoomTypeEnum.CHAT_ROOM.getValue()==room.getRoomType().intValue() || RoomTypeEnum.GAME_ROOM.getValue()==room.getRoomType().intValue()){
                for(ChrmEntrySetInfo setInfo:resultInfo.getKeys()){
                    if(setInfo.getKey().startsWith("RCSeatInfoSeatPartPrefixKey")&&setInfo.getValue().contains(statusSync.getUserId())){
                        String value = setInfo.getValue();
                        Map valueMap = (Map) GsonUtil.fromJson(value, Map.class);
                        Double status = (Double) valueMap.get("status");
                        if(status!=null&&status.intValue()==1){
                            newSetInfo = new ChrmEntrySetInfo();
                            BeanUtils.copyProperties(setInfo,newSetInfo);
                            valueMap.put("status",0);
                            valueMap.remove("userId");
                            newSetInfo.setValue(GsonUtil.toJson(valueMap));
                            newSetInfo.setChrmId(statusSync.getRoomId());
                            break;
                        }
                    }
                }
            }else if(RoomTypeEnum.VIDEO_ROOM.getValue()==room.getRoomType().intValue()){
                // 如果这个房间是这个人创建的，就不处理
                if(room.getUserId().equals(statusSync.getUserId())){
                    // 如果房主下线，那么直接删除房间
                    roomService.chrmDelete(statusSync.getRoomId());
                }else{
                    for(ChrmEntrySetInfo setInfo:resultInfo.getKeys()) {
                        if (setInfo.getKey().startsWith("LIVE_VIDEO_SEAT_INFO_PRE_") && setInfo.getValue().contains(statusSync.getUserId())) {
                            String value = setInfo.getValue();
                            Map valueMap = (Map) GsonUtil.fromJson(value, Map.class);
                            newSetInfo = new ChrmEntrySetInfo();
                            BeanUtils.copyProperties(setInfo,newSetInfo);
                            valueMap.put("userEnableVideo",true);
                            valueMap.put("userEnableAudio",true);
                            valueMap.put("userId","");
                            newSetInfo.setValue(GsonUtil.toJson(valueMap));
                            newSetInfo.setChrmId(statusSync.getRoomId());
                            break;
                        }
                    }
                }

            }else if(RoomTypeEnum.ELECT_ROOM.getValue()==room.getRoomType().intValue()){
                for(ChrmEntrySetInfo setInfo:resultInfo.getKeys()) {
                    if (setInfo.getKey().startsWith("RCRadioRoomKVSeatingKey")){
                        newSetInfo = new ChrmEntrySetInfo();
                        BeanUtils.copyProperties(setInfo,newSetInfo);
                        newSetInfo.setValue("0");
                        newSetInfo.setChrmId(statusSync.getRoomId());
                    }
                }
            }


            if(newSetInfo!=null){
                IMApiResultInfo imApiResultInfo = imHelper.entrySet(newSetInfo, 3);
                log.info("exception out entrySet result:{}",imApiResultInfo);
            }
            // 发送用户退出房间信息，不管在不在麦位上
            imHelper.publishSysMessage(uid,Lists.newArrayList(statusSync.getUserId()),message);

        } catch (Exception e) {
            log.info("exception out message:{}",e.getMessage(),e);
        }
        return RestResult.success();
    }
}
