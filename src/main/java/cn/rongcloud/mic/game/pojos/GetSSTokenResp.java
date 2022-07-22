package cn.rongcloud.mic.game.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 获取长期令牌响应
 * 注意：响应体中的字段命名格式为SNAKE_CASE，需配置spring.jackson.property-naming-strategy=SNAKE_CASE
 *
 * @author Sud
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GetSSTokenResp implements Serializable {
    /**
     * 长期令牌，字段名:ss_token
     */
    private String ss_token;

    /**
     * 长期令牌SSToken的过期时间（毫秒时间戳），字段名:expire_date
     */
    private long expire_date;

}


