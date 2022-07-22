package cn.rongcloud.mic.game.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取用户信息响应
 * 注意：响应体中的字段命名格式为SNAKE_CASE，需配置spring.jackson.property-naming-strategy=SNAKE_CASE
 *
 * @author Sud
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GetUserInfoResp {

    /**
     * 用户ID，字段名：uid
     */
    private String uid;

    /**
     * 昵称，字段名：nick_name
     */
    private String nick_name;

    /**
     * 头像地址（最优解为128*128），字段名：avatar_url
     */
    private String avatar_url;

    /**
     * 性别（男：male, 女：female），字段名：gender
     */
    private String gender;

}


