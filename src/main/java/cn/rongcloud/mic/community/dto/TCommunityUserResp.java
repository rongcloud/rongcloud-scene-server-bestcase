package cn.rongcloud.mic.community.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TCommunityUserResp {

    @ApiModelProperty("用户唯一id")
    private String userUid;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("头像")
    private String portrait;

    @ApiModelProperty("是否是创建者")
    private boolean creatorFlag;

    @ApiModelProperty("是否被禁言0:没有,1:禁言")
    private Integer shutUp;

    @ApiModelProperty("在线状态0：未在线，1：在言")
    private Integer onlineStatus;



}
