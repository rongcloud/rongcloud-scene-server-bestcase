package cn.rongcloud.mic.community.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TChannelUpdateReq {

    @ApiModelProperty("频道唯一标识")
    private String uid;

    @ApiModelProperty("频道名称")
    private String name;

    @ApiModelProperty("频道简介")
    private String remark;

}
