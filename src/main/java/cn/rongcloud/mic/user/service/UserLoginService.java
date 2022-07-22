package cn.rongcloud.mic.user.service;

import cn.rongcloud.mic.user.model.LoginLog;

import javax.servlet.http.HttpServletRequest;

public interface UserLoginService {

    void save(LoginLog loginLog);


    void saveByUserId(String userId, HttpServletRequest httpReq);

}
