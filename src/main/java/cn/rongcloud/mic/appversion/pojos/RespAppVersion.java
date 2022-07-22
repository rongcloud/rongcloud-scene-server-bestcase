package cn.rongcloud.mic.appversion.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/6/16
 */
@Data
public class RespAppVersion {

    @ApiModelProperty("平台")
    private String platform;

    @ApiModelProperty("下载地址")
    private String downloadUrl;

    @ApiModelProperty("版本号")
    private String version;

    @ApiModelProperty("版本标识")
    private Long versionCode;

    @ApiModelProperty("是否强制更新")
    private Boolean forceUpgrade;

    @ApiModelProperty("版本描述")
    private String releaseNote;
}
