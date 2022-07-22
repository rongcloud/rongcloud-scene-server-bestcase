package cn.rongcloud.mic.community.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TGroupSaveReq {

    @ApiModelProperty("社区唯一id")
    private String communityUid;

    @ApiModelProperty("分组名称")
    private String name;

}
