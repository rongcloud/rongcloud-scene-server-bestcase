package cn.rongcloud.mic.user.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/5/25.
 */
@Data
public class ResLogin {
    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("用户头像")
    private String portrait;

    @ApiModelProperty("用户类型，1 注册用户 0 游客")
    private Integer type;

    @ApiModelProperty("用户性别0:未知,1:男，2:女")
    private Integer sex;

    @ApiModelProperty("认证信息")
    private String authorization;

    @ApiModelProperty("IM 连接 token")
    private String imToken;

    @ApiModelProperty("当前时间戳，毫秒")
    private Long currentTime;
}
