package cn.rongcloud.mic.user.pojos;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/5/25.
 */
@Data
public class ReqLogin {
    @NotBlank(message = "deviceId should not be blank")
    @ApiModelProperty("设备id")
    private String deviceId;

    @ApiModelProperty("用户名")
    private String userName;

    @NotBlank(message = "mobile should not be blank")
    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("验证码")
    private String verifyCode;

    @ApiModelProperty("头像")
    private String portrait;

    @ApiModelProperty("区号")
    private String region;

    @ApiModelProperty("平台")
    private String platform="mobile";

    @ApiModelProperty("渠道")
    private String channel;

    @ApiModelProperty("渠道上报用的平台")
    private String platformType;

    @ApiModelProperty("版本号")
    private String version;


}
