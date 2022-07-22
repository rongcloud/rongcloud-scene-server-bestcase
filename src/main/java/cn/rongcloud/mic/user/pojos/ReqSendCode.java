package cn.rongcloud.mic.user.pojos;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/5/25.
 */
@Data
public class ReqSendCode {
    @NotBlank(message = "mobile should not be blank")
    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("区号")
    private String region;
}
