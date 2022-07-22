package cn.rongcloud.mic.room.controller;

import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.mic.common.annotation.RedisKeyRepeatSubmit;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.jwt.filter.JwtFilter;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.room.pojos.*;
import cn.rongcloud.mic.room.service.RoomService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/mic/room")
@Slf4j
@Api(tags = "房间 相关")
public class RoomController {

    @Autowired
    RoomService roomService;

    @ApiOperation("创建房间")
    @PostMapping(value = "/create")
    public RestResult createRoom(@RequestBody ReqRoomCreate data,
                                 @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser)
            throws Exception {
        log.info("create room, operator:{}, data:{}", jwtUser.getUserId(), GsonUtil.toJson(data));
        return roomService.createRoom(data, jwtUser);
    }

    @ApiOperation("房间列表")
    @GetMapping(value = "/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "房间类型",defaultValue = "1"),
            @ApiImplicitParam(name = "sex", value = "性别",required = false),
            @ApiImplicitParam(name = "gameId", value = "游戏id",required = false),
            @ApiImplicitParam(name = "page", value = "第几页",defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页条数",defaultValue = "10")
    })
    public RestResult<ResRoomList> getRoomList(@RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser,
                                               @RequestParam(value = "sex",required = false) Integer sex,
                                               @RequestParam(value = "gameId",required = false) String gameId,
                                               @RequestParam(value = "type", defaultValue = "1") Integer type,Integer page, Integer size, HttpServletRequest request) {
        log.info("get room list, operator:{},type:{} page:{}, size:{}", jwtUser.getUserId(), type, page, size);
        return roomService.getRoomPage(type,sex,gameId, page, size, request);
    }

    @GetMapping(value = "delete/all/{type}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "房间类型",required = true)
    })
    @ApiOperation("清空所有房间")
    @ApiIgnore
    public RestResult batchDel(@PathVariable("type") Integer type, HttpServletRequest request, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        RestResult result = roomService.getRoomList(type, 1, 999, request);
        int success = 0;
        int fail = 0;
        if (result.isSuccess()) {
            ResRoomList resRoomList = (ResRoomList) result.getResult();
            if (resRoomList.getRooms() != null && resRoomList.getRooms().size() > 0) {
                for (ResRoomInfo info : resRoomList.getRooms()) {
                    try {
                        RestResult restResult = roomService.chrmDelete(info.getRoomId());
                        if (restResult.isSuccess()) {
                            success++;
                        } else {
                            fail++;
                        }
                    } catch (Exception e) {
                        log.error("[room][delete is error][roomId:" + info.getRoomId() + "]");
                    }
                }
            }
        }
        return RestResult.success("执行完成，成功" + success + "条，失败" + fail + "条！");
    }

    @GetMapping(value = "/{roomId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "房间id",required = true)
    })
    @ApiOperation("获取房间信息")
    public RestResult<ResRoomInfo> getRoomDetail(@PathVariable("roomId") String roomId, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("get room info, operator:{}, roomId:{}", jwtUser.getUserId(), roomId);
        return roomService.getRoomDetail(roomId);
    }

    @ApiOperation("房间设置")
    @PutMapping(value = "/setting")
    @RedisKeyRepeatSubmit
    public RestResult roomSetting(@RequestBody ReqRoomSetting data, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) JwtUser jwtUser) {
        log.info("room setting, operator:{}, data:{}", jwtUser.getUserId(), GsonUtil.toJson(data));
        return roomService.roomSetting(data, jwtUser);
    }

    @ApiOperation("获取房间设置")
    @GetMapping(value = "/{roomId}/setting")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "房间id",required = true)
    })
    public RestResult<ReqRoomSetting> getRoomSetting(@PathVariable("roomId") String roomId, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("get room setting, operator:{}, roomId:{}", jwtUser.getUserId(), roomId);
        return roomService.getRoomSetting(roomId, jwtUser);
    }

    @ApiOperation("房间上锁解锁,只有主持人有权限操作")
    @PutMapping(value = "/private")
    @RedisKeyRepeatSubmit
    public RestResult roomPrivate(@RequestBody ReqRoomPrivate data, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("room private, operator:{}, data:{}", jwtUser.getUserId(), GsonUtil.toJson(data));
        return roomService.roomPrivate(data, jwtUser);
    }

    @ApiOperation(" 用户创建房间验证")
    @PutMapping(value = "/create/check")
    public RestResult createCheck(@RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("room create check, operator:{}", jwtUser.getUserId());
        return roomService.roomCreateCheck(jwtUser,null);
    }

    @ApiOperation(" 用户创建房间验证,一人多房间版本")
    @PutMapping(value = "/create/check/v1")
    public RestResult createCheck(@RequestBody(required = false) ReqRoomCheck data,
                                  @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("room create check v1, operator:{}, data:{}", jwtUser.getUserId(), GsonUtil.toJson(data));
        return roomService.roomCreateCheck(jwtUser,data==null?1:data.getRoomType());
    }

    @ApiOperation(" 房间管理员设置")
    @PutMapping(value = "/manage")
    @RedisKeyRepeatSubmit
    public RestResult roomManage(@RequestBody ReqRoomMicManage data, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) throws Exception {
        log.info("room manage, operator:{}, data:{}", jwtUser.getUserId(), GsonUtil.toJson(data));
        return roomService.roomManage(data, jwtUser);
    }

    @GetMapping(value = "/{roomId}/manage/list")
    @ApiOperation("房间管理员列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "房间id",required = true)
    })
    public RestResult roomManageList(@PathVariable("roomId") String roomId, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("room manage list, operator:{}, data:{}", jwtUser.getUserId(), roomId);
        return roomService.roomManageList(roomId, jwtUser);
    }

    @PutMapping(value = "/name")
    @ApiOperation(" 房间名称修改")
    public RestResult roomName(@RequestBody ReqRoomName data, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("room name, operator:{}, data:{}", jwtUser.getUserId(), GsonUtil.toJson(data));
        return roomService.roomName(data, jwtUser);
    }

    @PutMapping(value = "/background")
    @ApiOperation("房间背景修改")
    @RedisKeyRepeatSubmit
    public RestResult roomBackground(@RequestBody ReqRoomBackground data, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("room background, operator:{}, data:{}", jwtUser.getUserId(), GsonUtil.toJson(data));
        return roomService.roomBackground(data, jwtUser);
    }

    @GetMapping(value = "/{roomId}/members")
    @ApiOperation("获取房间成员列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "房间id",required = true)
    })
    public RestResult<List<RespRoomUser>> getRoomMembers(@PathVariable("roomId") String roomId, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser)
            throws Exception {
        log.info("get room members, operator:{}, roomId:{}", jwtUser.getUserId(), roomId);
        return roomService.getRoomMembers(roomId);
    }

    @GetMapping(value = "/{roomId}/members/v2")
    @ApiOperation("获取房间成员列表 v2版本")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "房间id",required = true)
    })
    public RestResult getRoomMembersV2(@PathVariable("roomId") String roomId, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser)
        throws Exception {
        log.info("get room members, operator:{}, roomId:{}", jwtUser.getUserId(), roomId);
        return roomService.getRoomMembersV2(roomId, jwtUser);
    }

    @PostMapping(value = "/gag")
    @ApiOperation("用户禁言设置")
    @RedisKeyRepeatSubmit
    public RestResult roomUserGag(@RequestBody ReqRoomUserGag data, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser)
            throws Exception {
        log.info("room user gag, operator:{}, data:{}", jwtUser.getUserId(), GsonUtil.toJson(data));
        return roomService.roomUserGag(data, jwtUser);
    }

    @GetMapping(value = "/{roomId}/gag/members")
    @ApiOperation("查询禁言用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "房间id",required = true)
    })
    public RestResult<List<RespRoomUser>> queryGagRoomUsers(@PathVariable("roomId") String roomId, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("query room gag users , operator:{}, roomId:{}", jwtUser.getUserId(), roomId);
        return roomService.queryGagRoomUsers(roomId, jwtUser);
    }

    @ApiOperation("发送聊天室广播消息")
    @PostMapping(value = "/message/broadcast")
    public RestResult messageBroadcast(@RequestBody ReqBroadcastMessage data, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser)
            throws Exception {
        log.info("message broadcast, operator:{}, data:{}", jwtUser.getUserId(), GsonUtil.toJson(data));
        return roomService.messageBroadcast(data, jwtUser);
    }

    @PostMapping(value = "/status_sync")
    @ApiOperation("IM 聊天室状态同步")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data", value = "同步数据",required = true)
    })
    public RestResult statusSync(@RequestBody List<ReqRoomStatusSync> data,
                                 @RequestParam(value = "signTimestamp") Long signTimestamp,
                                 @RequestParam(value = "nonce") String nonce,
                                 @RequestParam(value = "signature") String signature) {
        log.info("chartroom status sync, data:{}", GsonUtil.toJson(data));
        roomService.chrmStatusSync(data, signTimestamp, nonce, signature);
        return RestResult.success();
    }

    @GetMapping(value = "/{roomId}/delete")
    @ApiOperation("IM 删除聊天室")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "房间id",required = true)
    })
    @RedisKeyRepeatSubmit
    public RestResult statusSync(@PathVariable("roomId") String roomId, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) throws Exception {
        log.info("chartroom delete , data:{}", GsonUtil.toJson(roomId));
        roomService.chrmDelete(roomId);
        return RestResult.success();
    }

    @ApiOperation("赠送礼物")
    @PostMapping(value = "/gift/add")
    public RestResult giftAdd(@RequestBody ReqRoomGiftAdd data, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) JwtUser jwtUser)
            throws Exception {
        log.info("room gift add , operator:{}, data:{}", jwtUser.getUserId(), GsonUtil.toJson(data));
        return roomService.addGift(data, jwtUser);
    }

    @GetMapping(value = "/{roomId}/gift/list")
    @ApiOperation("赠送礼物列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "房间id",required = true)
    })
    public RestResult giftList(@PathVariable("roomId") String roomId, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser)
            throws Exception {
        log.info("room gift list , operator:{}, data:{}", jwtUser.getUserId(), roomId);
        return roomService.giftList(roomId, jwtUser);
    }

    @ApiOperation("切换房间类型的接口")
    @PostMapping("/toggle/type")
    @RedisKeyRepeatSubmit
    public RestResult toggleRoomType(@RequestBody ReqRoomType reqRoomType, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("room toggle roomType , operator:{}, data:{}", jwtUser.getUserId(), reqRoomType);
        return roomService.toggleRoomType(reqRoomType, jwtUser);
    }

    @ApiOperation("房间暂停接口,只有房主才能暂停暂停")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "房间id",required = true)
    })
    @GetMapping("/{roomId}/stop")
    @RedisKeyRepeatSubmit
    public RestResult stopRoom(@PathVariable("roomId") String roomId, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("room stop , operator:{}, data:{}", jwtUser.getUserId(), roomId);
        return roomService.stopRoom(roomId, jwtUser);
    }

    @ApiOperation("房间恢复接口,只有房主才能恢复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "房间id",required = true)
    })
    @RedisKeyRepeatSubmit
    @GetMapping("/{roomId}/start")
    public RestResult startRoom(@PathVariable("roomId") String roomId, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA)  @ApiIgnore JwtUser jwtUser) {
        log.info("room stop , operator:{}, data:{}", jwtUser.getUserId(), roomId);
        return roomService.startRoom(roomId, jwtUser);
    }

    @ApiOperation("在线房主列表")
    @GetMapping("/online/created/list")
    public RestResult<List<ResRoomInfo>> onLineCreated(@RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) JwtUser jwtUser) {
        log.info("room online create list , id:{}", jwtUser.getUserId());
        return roomService.onlineCreatedList(jwtUser,null);
    }

    @ApiOperation("在线房主列表,一人多房间版本")
    @GetMapping("/online/created/list/v1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomType",defaultValue = "1",value = "房间类型",required = false)
    })
    public RestResult<List<ResRoomInfo>> onLineCreated(@RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) JwtUser jwtUser,
                                                       @RequestParam(value = "roomType",defaultValue = "1",required = false) Integer roomType) {
        log.info("room online create list , id:{}", jwtUser.getUserId());
        return roomService.onlineCreatedList(jwtUser,roomType);
    }

    //@GetMapping("/pk/test")
    public RestResult testPush(String roomId) throws Exception {
        return roomService.testPush(roomId);
    }

    @GetMapping("/keepalive/add")
    public RestResult keepAliveRoom(@RequestParam("userId") String userId, @RequestParam("roomId") String roomId, @RequestParam("msg") String msg) {
        return roomService.keepAlive(userId, roomId, msg);
    }

    @GetMapping("/keepalive/cancel")
    public RestResult cancelKeepAlive(@RequestParam("userId") String userId, @RequestParam("roomId") String roomId, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) JwtUser jwtUser) {
        return roomService.cancelKeepAlive(userId, roomId);
    }

    @ApiOperation("查询某个用户是否在某个房间中,返回值：true代表在，false代表不在")
    @GetMapping("/user/exist")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true),
            @ApiImplicitParam(name = "roomId",value = "房间id",required = true)
    })
    public RestResult<Boolean> userExist(@RequestAttribute(value = JwtFilter.JWT_AUTH_DATA)  @ApiIgnore JwtUser jwtUser,
                                         @RequestParam("userId") String userId, @RequestParam("roomId") String roomId) {
        log.info("room userExist userId:{} , roomId:{}", userId,roomId);
        return roomService.userExist(roomId,userId);
    }

}
