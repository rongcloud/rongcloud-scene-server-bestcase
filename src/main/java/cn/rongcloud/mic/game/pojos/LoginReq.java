package cn.rongcloud.mic.game.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录请求
 * 注意：请求体中的字段命名格式为SNAKE_CASE，需配置spring.jackson.property-naming-strategy=SNAKE_CASE
 *
 * @author Sud
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LoginReq {
    /**
     * 用户ID（必填参数），字段名：user_id
     */
    @ApiModelProperty(value = "用户id",required = true)
    String userId;

}
