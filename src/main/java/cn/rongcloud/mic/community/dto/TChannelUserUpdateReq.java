package cn.rongcloud.mic.community.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TChannelUserUpdateReq {

    @ApiModelProperty("社区uid")
    private String communityUid;

    @ApiModelProperty("频道uid")
    private String channelUid;

    @ApiModelProperty("用户群通知设置,-1:和社区一致，0:所有消息都接收,1:被@时通知，2:从不通知")
    private Integer noticeType;
}
