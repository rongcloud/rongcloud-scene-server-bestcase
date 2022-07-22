package cn.rongcloud.common.whiteboard.rongcloud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by weiqinxiao on 2019/2/28.
 */
@Data
@Component
@ConfigurationProperties(prefix = "whiteboard.cn.rongcloud")
public class WhiteBoardProperties {
    private String host;
}
