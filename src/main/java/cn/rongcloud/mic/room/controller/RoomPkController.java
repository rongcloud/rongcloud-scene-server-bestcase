package cn.rongcloud.mic.room.controller;

import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.mic.common.annotation.RedisKeyRepeatSubmit;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.jwt.filter.JwtFilter;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.room.pojos.ReqRoomPk;
import cn.rongcloud.mic.room.pojos.ResRoomPkInfo;
import cn.rongcloud.mic.room.pojos.ResRoomPkScore;
import cn.rongcloud.mic.room.service.RoomPkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/mic/room")
@Slf4j
@Api(tags = "房间 PK 相关")
public class RoomPkController {

    @Autowired
    RoomPkService roomPkService;

    @ApiOperation("开始或停止 PK")
    @PostMapping("/pk")
    @RedisKeyRepeatSubmit
    public RestResult pk(@RequestBody ReqRoomPk data, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("room pk status , operator:{}, id:{}", GsonUtil.toJson(data), jwtUser.getUserId());
        return roomPkService.pk(data,jwtUser);
    }

    @GetMapping("/pk/info/{roomId}")
    @ApiOperation("获取房间PK 信息(单方)")
    @ApiImplicitParam(name = "roomId", value = "房间id",required = true)
    public RestResult<ResRoomPkInfo> pkInfo(@PathVariable("roomId") String roomId,
                                            @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("room pk info , operator:{}, id:{}", roomId, jwtUser.getUserId());
        return roomPkService.pkInfo(roomId);
    }

    @GetMapping("/pk/{roomId}/isPk")
    @ApiOperation("判断房间是否在PK中")
    @ApiImplicitParam(name = "roomId", value = "房间id",required = true)
    public RestResult<Boolean> roomIsPK(@PathVariable("roomId") String roomId,
                                           @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("room pk isPK list , roomId：{} ,id:{}", roomId, jwtUser.getUserId());
        return roomPkService.isPK(roomId);
    }

    @GetMapping("/pk/detail/{roomId}")
    @ApiOperation("获取PK 双方的房间信息")
    @ApiImplicitParam(name = "roomId", value = "房间id",required = true)
    public RestResult<ResRoomPkScore> getPkDetail(@PathVariable("roomId") String roomId) throws Exception {
        return roomPkService.getPkDetail(roomId);
    }

}
