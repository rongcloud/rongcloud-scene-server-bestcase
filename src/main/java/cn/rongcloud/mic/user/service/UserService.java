package cn.rongcloud.mic.user.service;


import cn.rongcloud.mic.authorization.pojos.ResLoginWX;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.user.model.TUser;
import cn.rongcloud.mic.user.pojos.ReqLogin;
import cn.rongcloud.mic.user.pojos.ReqLoginWX;
import cn.rongcloud.mic.user.pojos.ReqSendCode;
import cn.rongcloud.mic.user.pojos.ReqUpdate;
import cn.rongcloud.mic.user.pojos.ReqUserIds;
import cn.rongcloud.mic.user.pojos.ReqVisitorLogin;
import cn.rongcloud.mic.user.pojos.ResFollowInfo;
import cn.rongcloud.mic.user.pojos.ResLogin;
import cn.rongcloud.mic.user.pojos.ResRefreshToken;
import cn.rongcloud.mic.user.pojos.ResUserInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by sunyinglong on 2020/6/3
 */
public interface UserService {
    //手机用户登录
    RestResult<ResLogin> login(ReqLogin data, HttpServletRequest httpReq);

    //微信登录
    RestResult<ResLogin> loginWX(ReqLogin data, HttpServletRequest httpReq);

    RestResult<ResLoginWX> loginWXV2(ReqLoginWX data,HttpServletRequest httpReq);

    RestResult<ResLogin> visitorLogin(ReqVisitorLogin loginData);

    RestResult<ResRefreshToken> refreshIMToken(JwtUser jwtUser);

    RestResult sendCode(String mobile);

    TUser getUserInfo(String uid);

    RestResult<List<ResFollowInfo>> batchGetUsersInfo(ReqUserIds data, JwtUser jwtUser);

    RestResult update(ReqUpdate data, JwtUser jwtUser);

    /**
     * 根据 手机号 查询 用户信息
     * 每个用户一天最多查询20次
     * @param mobile
     * @param jwtUser
     * @return
     */
    RestResult<ResUserInfo> getUserByMobile(String mobile, JwtUser jwtUser);

    /**
     * 更改用户状态
     * 判断用户是否 在房间
     * @param roomId
     * @param jwtUser
     * @return
     */
    RestResult changeStatus(String roomId, JwtUser jwtUser);

    RestResult checkStatus(JwtUser jwtUser);



    RestResult sendBroadcastMessage(String msg);

    /**
     * @Description:微信小程序登录
     * @Param: [data]
     * @return: cn.rongcloud.mic.common.rest.RestResult
     * @Author: gaoxin
     * @Date: 2021/9/3
     */
    RestResult wechatAppletLogin(ReqLogin data);


    /**
     * 注销
     * @param jwtUser
     * @return
     */
    RestResult resign(JwtUser jwtUser);

    /**
     * 发送国际短信验证码
     * @param data
     * @return
     */
    RestResult sendInternationalCode(ReqSendCode data);

    /**
     * 国际版登录
     * @param data
     * @param isSMSVerify
     * @return
     */
    RestResult<ResLogin> internationalLogin(ReqLogin data, boolean isSMSVerify, HttpServletRequest httpReq);

    /**
     * 登录设备上报
     * @param platform
     * @param userId
     * @return
     */
    RestResult<ResLogin> loginDevice(String platform, String userId, HttpServletRequest httpReq);

    void freezeUser(String userId, Integer status, Date freezeTime);
}
