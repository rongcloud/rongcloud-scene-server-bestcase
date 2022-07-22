package cn.rongcloud.mic.community.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TCommunityUserInfoReq {

    @ApiModelProperty(value = "社区唯一标识",required = true)
    private String communityUid;

    @ApiModelProperty(value = "用户唯一标识",required = true)
    private String userUid;
}
