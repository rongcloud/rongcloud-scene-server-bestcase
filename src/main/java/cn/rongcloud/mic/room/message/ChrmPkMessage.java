package cn.rongcloud.mic.room.message;

import cn.rongcloud.common.im.BaseMessage;
import cn.rongcloud.mic.room.pojos.RespRoomUser;
import lombok.Data;

import java.util.List;

@Data
public class ChrmPkMessage extends BaseMessage {

    private Long score;

    private String roomId;

    private List<RespRoomUser> userList;

    private Long pkTime;

    @Override
    public String getObjectName() {
        return "RCMic:chrmPkMsg";
    }
}
