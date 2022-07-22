package cn.rongcloud.mic.community.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TCommunitySaveReq {

    @ApiModelProperty(value = "社区名称",required = true)
    private String name;

    @ApiModelProperty(value = "社区头像",required = true)
    private String portrait;

}
