package cn.rongcloud.mic.user.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/5/25.
 */
@Data
public class ResRefreshToken {

    @ApiModelProperty("IM 连接 token")
    private String imToken;
}
