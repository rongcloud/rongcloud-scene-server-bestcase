package cn.rongcloud.mic.community.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TChannelMessageSaveReq {

    @ApiModelProperty(value = "频道唯一id",required = true)
    private String channelUid;

    @ApiModelProperty(value = "消息唯一id",required = true)
    private String messageUid;
}
