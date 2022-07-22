package cn.rongcloud.mic.common.jwt.filter;

import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.mic.common.constant.CustomerConstant;
import cn.rongcloud.mic.common.jwt.JwtTokenHelper;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.jwt.enums.UserAgentTypeEnum;
import cn.rongcloud.mic.common.utils.JwtUtils;
import cn.rongcloud.mic.common.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by sunyinglong on 2020/6/25
 */
@Slf4j
@Component
public class JwtFilter extends GenericFilterBean {
    public static final String JWT_AUTH_DATA = "JWT_AUTH_DATA";
    public static final String USER_AGENT_TYPE = "USER_AGENT_TYPE";

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private JwtTokenHelper tokenHelper;

    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;

        log.debug("doFilter: " + httpReq.getRequestURL().toString());

        final String token = httpReq.getHeader("Authorization");
        if (token != null) {
            try {
                String jwtUser = JwtUtils.getBusinessValue(token, CustomerConstant.SERVICENAME);
                JwtUser user = (JwtUser) GsonUtil.fromJson(jwtUser, JwtUser.class);
                if (null != user) {
                    httpReq.setAttribute(JWT_AUTH_DATA, user);
                }
            } catch (Exception e) {
                log.error("caught error when check token:", e);
            }
        } else {
            log.error("not found Authorization");
        }

        String userAgent = httpReq.getHeader("user-agent");
        log.info("the request IP:{}, UA: {}", IpUtil.getIpAddr(httpReq), userAgent);
        if (null != userAgent) {
            UserAgentTypeEnum type = UserAgentTypeEnum.getEnumByUserAgent(userAgent);
            httpReq.setAttribute(USER_AGENT_TYPE, type);
        }

        chain.doFilter(req, res);
    }

/*    public  String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        if (ip.split(",").length > 1) {
            ip = ip.split(",")[0];
        }
        return ip;
    }*/
}
