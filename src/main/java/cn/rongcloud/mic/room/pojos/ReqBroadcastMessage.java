package cn.rongcloud.mic.room.pojos;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/6/8
 */
@Data
public class ReqBroadcastMessage {

    @ApiModelProperty("发送人用户id")
    private String fromUserId;

    @NotBlank(message = "objectName should not be blank")
    @ApiModelProperty("消息类型，参考融云消息类型表.消息标志.可自定义消息类型，长度不超过 32 个字符，您在自定义消息时需要注意，不要以 \"RC:\" 开头，以避免与融云系统内置消息的 ObjectName 重名。（必传）")
    private String objectName; //消息类型，参考融云消息类型表.消息标志；可自定义消息类型，长度不超过 32 个字符，您在自定义消息时需要注意，不要以 "RC:" 开头，以避免与融云系统内置消息的 ObjectName 重名。（必传）

    @NotBlank(message = "content should not be blank")
    @ApiModelProperty("发送内容")
    private String content;
}
