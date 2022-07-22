package cn.rongcloud.mic.room.message;

import cn.rongcloud.common.im.BaseMessage;
import cn.rongcloud.mic.room.pojos.ResRoomScore;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChrmPkStatusMessage extends BaseMessage {

    private String stopPkRoomId;

    private int statusMsg;

    private List<ResRoomScore> roomScores;

    private Long timeDiff;

    @Override
    public String getObjectName() {
        return "RCMic:chrmPkStatusMsg";
    }
}
