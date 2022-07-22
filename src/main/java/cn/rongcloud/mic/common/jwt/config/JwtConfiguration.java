package cn.rongcloud.mic.common.jwt.config;

import cn.rongcloud.mic.common.jwt.JwtTokenHelper;
import cn.rongcloud.mic.common.jwt.filter.JwtFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by sunyinglong on 2020/6/25
 */
@Slf4j
@Configuration
public class JwtConfiguration {

    @Bean
    public JwtTokenHelper jwtTokenHelper(JwtProperties jwtProperties) {
        log.info("config jwtTokenHelper expired {} ms", jwtProperties.getTtlInMilliSec());
        return jwtProperties.getTtlInMilliSec() == null
                ? new JwtTokenHelper(jwtProperties.getSecret())
                : new JwtTokenHelper(jwtProperties.getSecret(), jwtProperties.getTtlInMilliSec());
    }

    @Bean
    public FilterRegistrationBean jwtTokenFilter(JwtFilter jwtFilter) {
        log.info("config jwtTokenFilter");
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(jwtFilter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
