package cn.rongcloud.mic.user.pojos;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/5/25.
 */
@Data
public class ReqVisitorLogin {
    @NotBlank(message = "deviceId should not be blank")
    @ApiModelProperty("设备id")
    private String deviceId;

    @NotBlank(message = "userName should not be blank")
    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("头像")
    private String portrait;
}
