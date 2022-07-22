package cn.rongcloud.mic.appversion.pojos;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/6/16
 */
@Data
public class ReqAppVersionCreate {

    @NotBlank(message = "platform should not be blank")
    @ApiModelProperty("平台")
    private String platform;

    @NotBlank(message = "downloadUrl should not be blank")
    @ApiModelProperty("下载地址")
    private String downloadUrl;

    @NotBlank(message = "version should not be blank")
    @ApiModelProperty("版本号")
    private String version;

/*    @NotBlank(message = "versionCode should not be blank")
    @ApiModelProperty("版本标识")
    private Long versionCode;*/

    @NotBlank(message = "forceUpgrade should not be blank")
    @ApiModelProperty("是否强制更新")
    private Boolean forceUpgrade;

    @ApiModelProperty("版本描述")
    private String releaseNote;
}
