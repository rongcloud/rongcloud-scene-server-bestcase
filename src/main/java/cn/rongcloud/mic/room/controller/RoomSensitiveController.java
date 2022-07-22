package cn.rongcloud.mic.room.controller;

import cn.rongcloud.mic.common.annotation.RedisKeyRepeatSubmit;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.jwt.filter.JwtFilter;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.room.pojos.ResSensitive;
import cn.rongcloud.mic.room.service.RoomSensitiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/mic/room")
@Slf4j
@Api(tags = "房间敏感词管理")
public class RoomSensitiveController {

    @Autowired
    private RoomSensitiveService roomSensitiveService;

    @ApiOperation("添加敏感词,只有房主才能添加")
    @PostMapping("/sensitive/add")
    @RedisKeyRepeatSubmit
    public RestResult sensitiveAdd(@RequestBody ResSensitive resSensitive, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("room sensitive add , operator:{}, data:{}", jwtUser.getUserId(), resSensitive);
        return roomSensitiveService.sensitiveAdd(resSensitive, jwtUser);
    }

    @ApiOperation("编辑敏感词,只有房主才能编辑敏感词")
    @PutMapping("/sensitive/edit")
    @RedisKeyRepeatSubmit
    public RestResult sensitiveEdit(@RequestBody ResSensitive resSensitive, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("room sensitive edit , operator:{}, data:{}", jwtUser.getUserId(), resSensitive);
        return roomSensitiveService.sensitiveEdit(resSensitive, jwtUser);
    }

    @GetMapping("/sensitive/del/{id}")
    @ApiOperation("删除敏感词,只有房主才能删除敏感词")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "敏感词id",required = true)
    })
    @RedisKeyRepeatSubmit
    public RestResult sensitiveDelete(@PathVariable(value = "id", required = true) Long id, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("room sensitive delete , operator:{}, id:{}", jwtUser.getUserId(), id);
        return roomSensitiveService.sensitiveDelete(id, jwtUser);
    }

    @GetMapping("/sensitive/{roomId}/list")
    @ApiOperation("敏感词列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "房间ID",required = true)
    })
    public RestResult sensitiveList(@PathVariable("roomId") String roomId, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("room sensitive list , operator:{}, id:{}", jwtUser.getUserId(), roomId);
        return roomSensitiveService.sensitiveList(roomId, jwtUser);
    }

}
