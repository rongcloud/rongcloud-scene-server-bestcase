package cn.rongcloud.mic.user.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ResFollowInfo {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("头像")
    private String portrait;

    @ApiModelProperty("是否关注 0 未关注 1 已关注")
    private Integer status;
}
