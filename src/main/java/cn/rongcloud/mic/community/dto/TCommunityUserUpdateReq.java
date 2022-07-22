package cn.rongcloud.mic.community.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TCommunityUserUpdateReq {

    @ApiModelProperty(value = "社区唯一标识",required = true)
    private String communityUid;

    @ApiModelProperty(value = "用户唯一标识,被设置的用户",required = true)
    private String userUid;

    @ApiModelProperty("用户群昵称")
    private String nickName;

    @ApiModelProperty("用户群通知设置,0:所有消息都接收,1:被@时通知，2:从不通知")
    private Integer noticeType;

    @ApiModelProperty("是否被封禁0:没有,1:封禁")
    private Integer freezeFlag;

    @ApiModelProperty("是否被禁言0:没有,1:禁言")
    private Integer shutUp;

    @ApiModelProperty("2:审核未通过,3:审核通过,4:退出，5：被踢出")
    private Integer status;


}
