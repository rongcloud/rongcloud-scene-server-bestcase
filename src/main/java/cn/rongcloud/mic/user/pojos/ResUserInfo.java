package cn.rongcloud.mic.user.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
public class ResUserInfo {

    @ApiModelProperty("用户唯一id")
    private String uid;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("头像")
    private String portrait;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("用户类型，0:注册用户 1:游客")
    private Integer type;

    @ApiModelProperty("设备id")
    private String deviceId;

    private Date createDt;

    private Date updateDt;

    @ApiModelProperty("所属房间ID")
    private String belongRoomId;
}
