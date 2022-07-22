package cn.rongcloud.mic.user.service;

import cn.rongcloud.mic.common.utils.IpUtil;
import cn.rongcloud.mic.user.mapper.UserLoginLogMapper;
import cn.rongcloud.mic.user.model.LoginLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Service
@Slf4j
public class UserLoginServiceImpl implements UserLoginService{

    @Resource
    UserLoginLogMapper userLoginLogMapper;

    @Override
    public void save(LoginLog loginLog) {
        userLoginLogMapper.insert(loginLog);
    }

    @Override
    public void saveByUserId(String userId, HttpServletRequest httpReq) {
        Date date = new Date();
        String ipAddr = IpUtil.getIpAddr(httpReq);
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(userId);
        loginLog.setIp(ipAddr);
        loginLog.setCreateTime(date);
        loginLog.setUpdateTime(date);
        this.save(loginLog);
    }


}
