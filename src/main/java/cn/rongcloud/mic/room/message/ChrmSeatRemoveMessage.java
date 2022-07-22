package cn.rongcloud.mic.room.message;

import cn.rongcloud.common.im.BaseMessage;
import lombok.Data;

@Data
public class ChrmSeatRemoveMessage extends BaseMessage {

    private String roomId;

    private String userId;

    private Long timestamp;

    @Override
    public String getObjectName() {
        return "RCMic:chrmSeatRemoveMsg";
    }
}
