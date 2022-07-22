package cn.rongcloud.mic.community.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TGroupUpdateReq {


    @ApiModelProperty("分组唯一标识")
    private String uid;

    @ApiModelProperty("分组名称")
    private String name;

}
