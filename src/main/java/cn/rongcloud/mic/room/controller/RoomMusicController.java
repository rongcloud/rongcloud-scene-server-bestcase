package cn.rongcloud.mic.room.controller;

import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.mic.common.annotation.RedisKeyRepeatSubmit;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.jwt.filter.JwtFilter;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.rest.RestResultCode;
import cn.rongcloud.mic.room.model.TRoomMusic;
import cn.rongcloud.mic.room.pojos.ReqRoomMusicAdd;
import cn.rongcloud.mic.room.pojos.ReqRoomMusicDelete;
import cn.rongcloud.mic.room.pojos.ReqRoomMusicList;
import cn.rongcloud.mic.room.pojos.ReqRoomMusicMove;
import cn.rongcloud.mic.room.service.RoomMusicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/mic/room")
@Slf4j
@Api(tags = "房间音乐管理")
public class RoomMusicController {

    @Autowired
    private RoomMusicService roomMusicService;

    @ApiOperation("聊天室音乐列表")
    @PostMapping(value = "/music/list")
    public RestResult<List<TRoomMusic>> musicList(@RequestBody ReqRoomMusicList data, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("chartroom music list, data:{}", GsonUtil.toJson(data));
        return roomMusicService.musicList(data);
    }

    @ApiOperation("添加音乐")
    @PostMapping(value = "/music/add")
    @RedisKeyRepeatSubmit
    public RestResult musicAdd(@RequestBody ReqRoomMusicAdd data, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser)
            throws Exception {
        log.info("room music add , operator:{}, data:{}", jwtUser, GsonUtil.toJson(data));
        return roomMusicService.addMusic(data, jwtUser);
    }

    @ApiOperation("移动音乐")
    @PostMapping(value = "/music/move")
    @RedisKeyRepeatSubmit
    public RestResult musicMove(@RequestBody ReqRoomMusicMove data, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) JwtUser jwtUser)
            throws Exception {
        log.info("room music move , operator:{}, data:{}", jwtUser, GsonUtil.toJson(data));
        return roomMusicService.musicMove(data, jwtUser);
    }

    @ApiOperation("聊天室音乐删除")
    @PostMapping(value = "/music/delete")
    @RedisKeyRepeatSubmit
    public RestResult musicDelete(@RequestBody ReqRoomMusicDelete data, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("chartroom music delete, data:{}", GsonUtil.toJson(data));
        return roomMusicService.musicDelete(data);
    }

    /**
     * hfive 音乐需求，中途进入房间的人要知道正在播放的音乐
     * @param data 如果ID 为空就是这个房间停止播放音乐，如果不为空就是播放音乐
     * @param jwtUser
     * @return
     */
    @ApiOperation("播放音乐/ 停止播放音乐，id 为空则为停止")
    @PostMapping(value = "/music/play")
    @RedisKeyRepeatSubmit
    public RestResult musicPlay(@RequestBody ReqRoomMusicDelete data, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("chartroom music play, data:{}", GsonUtil.toJson(data));
        if(StringUtils.isEmpty(data.getRoomId())){
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        return roomMusicService.musicPlay(data);
    }

    @ApiOperation("正在播放的音乐")
    @GetMapping(value = "/music/play/{roomId}")
    public RestResult<TRoomMusic> musicPlayGet(@PathVariable("roomId") String roomId, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("chartroom music play get, roomId:{}", GsonUtil.toJson(roomId));
        return roomMusicService.musicPlayGet(roomId);
    }

}
