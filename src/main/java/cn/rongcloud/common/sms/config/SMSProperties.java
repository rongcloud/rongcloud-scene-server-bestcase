package cn.rongcloud.common.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by sunyinglong on 2020/6/25
 */
@Data
@Component
@ConfigurationProperties(prefix = "sms")
public class SMSProperties {
    private String appKey;
    private String secret;
    private String host;
    private String templateId;
}
