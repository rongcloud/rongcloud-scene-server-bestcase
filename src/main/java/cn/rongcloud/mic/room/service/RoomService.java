package cn.rongcloud.mic.room.service;

import cn.rongcloud.mic.common.exception.BusinessException;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.room.enums.ChrTaskType;
import cn.rongcloud.mic.room.model.TRoom;
import cn.rongcloud.mic.room.pojos.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by sunyinglong on 2020/6/3
 */
public interface RoomService {

    RestResult createRoom(ReqRoomCreate data, JwtUser jwtUser) throws Exception;

    RestResult<ResRoomInfo> getRoomDetail(String roomId);

    RestResult<ResRoomList> getRoomList(Integer type, Integer page, Integer size, HttpServletRequest request);

    List<TRoom> getRoomListAll();

    void removeRoom(TRoom room) throws Exception;

    RestResult roomSetting(ReqRoomSetting data, JwtUser jwtUser);

    RestResult roomUserGag(ReqRoomUserGag data, JwtUser jwtUser) throws Exception;

    RestResult<List<RespRoomUser>> getRoomMembers(String roomId) throws Exception;

    RestResult getRoomMembersV2(String roomId, JwtUser jwtUser) throws Exception;

    RestResult<List<RespRoomUser>> queryGagRoomUsers(String roomId, JwtUser jwtUser);

    void chrmStatusSync(List<ReqRoomStatusSync> data, Long signTimestamp, String nonce, String signature);

    RestResult messageBroadcast(ReqBroadcastMessage data, JwtUser jwtUser) throws Exception;

    RestResult roomPrivate(ReqRoomPrivate data, JwtUser jwtUser);

    RestResult roomName(ReqRoomName data, JwtUser jwtUser);

    RestResult roomManage(ReqRoomMicManage data, JwtUser jwtUser) throws Exception;

    RestResult roomManageList(String roomId, JwtUser jwtUser);

    RestResult chrmDelete(String roomId) throws Exception;

    RestResult roomBackground(ReqRoomBackground data, JwtUser jwtUser);

    RestResult roomCreateCheck(JwtUser jwtUser, Integer roomType);

    RestResult addGift(ReqRoomGiftAdd data, JwtUser jwtUser);

    RestResult giftList(String roomId, JwtUser jwtUser);

    RestResult<ReqRoomSetting> getRoomSetting(String roomId, JwtUser jwtUser);

    RestResult toggleRoomType(ReqRoomType reqRoomType, JwtUser jwtUser);

    RestResult stopRoom(String roomId, JwtUser jwtUser);

/*    RestResult sensitiveAdd(ResSensitive resSensitive, JwtUser jwtUser);

    RestResult sensitiveEdit(ResSensitive resSensitive, JwtUser jwtUser);

    RestResult sensitiveDelete(Long id, JwtUser jwtUser);

    RestResult sensitiveList(String roomId, JwtUser jwtUser);*/

    RestResult<List<ResRoomInfo>> onlineCreatedList(JwtUser jwtUser, Integer roomType);

    RestResult startRoom(String roomId, JwtUser jwtUser);

    RestResult testPush(String roomId) throws Exception;

    RestResult checkData(String roomId, JwtUser jwtUser);

    void updateRoomCache(TRoom room);

    TRoom getRoomInfo(String roomId);

    RestResult callImToKeepAlive(ChrTaskType chrTaskType);

    RestResult keepAlive(String userId, String roomId, String msg);

    RestResult cancelKeepAlive(String userId, String roomId);

    RestResult<ResRoomList> getRoomPage(Integer type, Integer sex, String gameId, Integer page, Integer size, HttpServletRequest request);

    ResRoomInfo build(TRoom room);

    Integer getRoomUserTotal(String roomId);

    void roomManageUserCheck(TRoom room, String userId) throws BusinessException;

    RestResult<Boolean> userExist(String roomId, String userId);
}
