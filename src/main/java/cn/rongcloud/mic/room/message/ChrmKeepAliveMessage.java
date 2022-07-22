package cn.rongcloud.mic.room.message;

import cn.rongcloud.common.im.BaseMessage;
import lombok.Data;

@Data
public class ChrmKeepAliveMessage extends BaseMessage {

    private String content;

    @Override
    public String getObjectName() {
        return "RCMic:keepAlive";
    }
}
