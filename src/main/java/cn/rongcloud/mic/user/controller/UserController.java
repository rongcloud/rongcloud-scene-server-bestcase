package cn.rongcloud.mic.user.controller;

import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.mic.common.annotation.RedisKeyRepeatSubmit;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.jwt.filter.JwtFilter;
import cn.rongcloud.mic.room.pojos.ResRoomInfo;
import cn.rongcloud.mic.room.service.RoomService;
import cn.rongcloud.mic.user.model.TUser;
import cn.rongcloud.mic.user.pojos.*;
import cn.rongcloud.mic.user.service.UserFollowServiceImpl;
import cn.rongcloud.mic.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户管理")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserFollowServiceImpl userFollowService;

    @Autowired
    RoomService roomService;

    @ApiOperation("游客登录")
    @PostMapping(value = "/visitorLogin")
    @RedisKeyRepeatSubmit
    public RestResult<ResLogin> visitorLogin(@RequestBody ReqVisitorLogin loginData) {
        log.info("visitor login: {}", GsonUtil.toJson(loginData));
        return userService.visitorLogin(loginData);
    }

    @ApiOperation(" 刷新 token")
    @PostMapping(value = "/refreshToken")
    public RestResult<ResRefreshToken> refreshToken(@RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("refreshToken, jwtUser: {}", GsonUtil.toJson(jwtUser));
        return userService.refreshIMToken(jwtUser);
    }

    @ApiOperation("更新用户信息")
    @PostMapping(value = "/update")
    @RedisKeyRepeatSubmit
    public RestResult update(@RequestBody ReqUpdate data, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("user update data, jwtUser: {}", GsonUtil.toJson(data), GsonUtil.toJson(jwtUser));
        return userService.update(data, jwtUser);
    }

    @ApiOperation("发送短信验证码")
    @PostMapping(value = "/sendCode")
    @RedisKeyRepeatSubmit
    public RestResult sendCode(@RequestBody ReqSendCode data) {
        log.info("sendCode, data: {}", GsonUtil.toJson(data));
        if(StringUtils.isBlank(data.getRegion())||data.getRegion().equals("+86")){
            return userService.sendCode(data.getMobile());
        }else{
            return userService.sendInternationalCode(data);
        }

    }

    @ApiOperation("用户登录")
    @PostMapping(value = "/login")
    public RestResult<ResLogin> login(@RequestBody ReqLogin data, HttpServletRequest httpReq) {
        log.info("user login: {}", GsonUtil.toJson(data));
        if(StringUtils.isBlank(data.getRegion())||data.getRegion().equals("+86")){
            return userService.login(data,httpReq);
        }else{
            return userService.internationalLogin(data,true,httpReq);
        }

    }

    @ApiOperation("用户登录设备上报")
    @PostMapping(value = "/login/device/{platform}")
    public RestResult<ResLogin> loginDevice(@PathVariable("platform") String platform, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser,HttpServletRequest httpReq) {
        log.info("user login device platform : {},operator:{}",platform,jwtUser.getUserId());
        return userService.loginDevice(platform,jwtUser.getUserId(), httpReq);
    }
    /**
     * 注销账号
     * @param jwtUser
     * @return
     */
    @PostMapping(value = "/resign")
    @ApiOperation("注销接口")
    @RedisKeyRepeatSubmit
    public RestResult resign (@RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("user resign: {}", GsonUtil.toJson(jwtUser));
        return userService.resign (jwtUser);
    }

    @ApiOperation("微信用户登录")
    @PostMapping(value = "/wx/login")
    public RestResult<ResLogin> loginWX(@RequestBody ReqLogin data,HttpServletRequest httpReq) {
        log.info("user wechat login: {}", GsonUtil.toJson(data));
        return userService.loginWX(data,httpReq);
    }

    @ApiOperation("批量获取用户信息")
    @PostMapping(value = "/batch")
    public RestResult<List<ResFollowInfo>> batch(@RequestBody ReqUserIds data, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("batch get user, operator: {}, data:{}", jwtUser.getUserId(), GsonUtil.toJson(data));
        return userService.batchGetUsersInfo(data, jwtUser);
    }

    @ApiOperation("批量获取用户信息")
    @GetMapping("get/{mobile}")
    @ApiImplicitParam(name = "mobile", value = "手机号",required = true)
    public RestResult<ResUserInfo> getByMobile(@PathVariable("mobile") String mobile, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("by mobile get user, operator: {},  mobile:{}", jwtUser.getUserId(), mobile);
        return userService.getUserByMobile(mobile, jwtUser);
    }

    @ApiOperation("更改用户所属房间")
    @ApiImplicitParam(name = "roomId", value = "房间id",required = true)
    @GetMapping("change")
    @RedisKeyRepeatSubmit
    public RestResult changeStatus(String roomId, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("user change status, operator: {},  roomId:{}", jwtUser.getUserId(), roomId);
        return userService.changeStatus(roomId, jwtUser);
    }


    @ApiOperation("检查当前用户所属房间")
    @GetMapping("check")
    public RestResult<ResRoomInfo> checkStatus(@RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("user check status, operator: {}", jwtUser.getUserId());
        RestResult restResult = userService.checkStatus(jwtUser);

        //解决循环依赖问题
        if (restResult.isSuccess()) {
            if (restResult.getResult() != null) {
                TUser tUser = (TUser) restResult.getResult();
                if (StringUtils.isNotBlank(tUser.getBelongRoomId())) {
                    RestResult restResult1 = roomService.getRoomDetail(tUser.getBelongRoomId());
                    return restResult1;
                }
            }
            return RestResult.success();
        }

        return restResult;
    }

    @ApiOperation("用户关注/取消关注")
    @ApiImplicitParam(name = "userId", value = "用户id",required = true)
    @GetMapping("follow/{userId}")
    public RestResult follow(@PathVariable("userId") String userId, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("user follow, operator: {} {}", jwtUser.getUserId(), userId);
        RestResult restResult = userFollowService.follow(jwtUser,userId);
        return restResult;
    }

    @GetMapping("follow/list")
    @ApiOperation("用户关注列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "1 我关注的 2 我的粉丝",defaultValue = "1"),
            @ApiImplicitParam(name = "page", value = "第几页",defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页条数",defaultValue = "10")
    })
    public RestResult followList(@RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser,
                                 @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size,@RequestParam(value = "type", defaultValue = "1") Integer type) {
        log.info("user follow list, operator: {} type :{}", jwtUser.getUserId(), type);
        RestResult restResult = userFollowService.followList(jwtUser,type,page,size);
        return restResult;
    }

    /**
     * @Description:微信小程序登录
     * @Param: [data]
     * @return: cn.rongcloud.mic.common.rest.RestResult
     * @Author: gaoxin
     * @Date: 2021/9/3
     */
    @PostMapping("wechatAppletLogin")
    @ApiOperation(value = "微信小程序登录")
    @ApiIgnore
    public RestResult wechatAppletLogin(@RequestBody ReqLogin data) {
        log.info("user wechatAppletLogin: {}\", GsonUtil.toJson(data)");
        return userService.wechatAppletLogin(data);
    }


/*    @ApiOperation("发送国际短信验证码")
    @PostMapping(value = "/international/sendCode")
    public RestResult sendInternationalCode(@RequestBody ReqSendCode data) {
        log.info("sendCode, data: {}", GsonUtil.toJson(data));
        return userService.sendInternationalCode(data);
    }

    @ApiOperation("国际用户登录")
    @PostMapping(value = "/international/login")
    public RestResult<ResLogin> internationalLogin(@RequestBody ReqLogin data) {
        log.info("user login: {}", GsonUtil.toJson(data));
        return userService.internationalLogin(data,true);
    }*/

}
