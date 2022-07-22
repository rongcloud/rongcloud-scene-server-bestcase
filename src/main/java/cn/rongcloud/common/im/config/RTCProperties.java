package cn.rongcloud.common.im.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by sunyinglong on 2020/6/25
 */
@Data
@Component
@ConfigurationProperties(prefix = "rtc")
public class RTCProperties {
    private String host;
}
