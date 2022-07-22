package cn.rongcloud.common.im.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by sunyinglong on 2020/6/25
 */
@Data
@Component
@ConfigurationProperties(prefix = "media.rcx")
public class RCXFileProperties {
    private String appKey;
    private String secret;
    private String host;
    private String bucketName;
    private String uploadUrl;
    private String domain;
}
