package cn.rongcloud.mic.game.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.sud.mgp.auth.api.SudMGPAuth;

/**
 * SudMGPAuth配置类
 *
 * @author Sud
 */
@Configuration
public class SudMGPAuthConfig {

    @Value("${game.appId}")
    private String gameAppId;

    @Value("${game.appSecret}")
    private String gameAppSecret;

    /**
     * 创建SUD服务端SDK鉴权对象
     *
     * @return SUD服务端SDK鉴权对象
     */
    @Bean
    public SudMGPAuth sudMGPAuth() {
        return new SudMGPAuth(gameAppId, gameAppSecret);
    }
}
