package cn.rongcloud.mic.community.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TChannelSaveReq {

    @ApiModelProperty("社区唯一id")
    private String communityUid;

    @ApiModelProperty("分组唯一id")
    private String groupUid;

    @ApiModelProperty("频道名称")
    private String name;

}
