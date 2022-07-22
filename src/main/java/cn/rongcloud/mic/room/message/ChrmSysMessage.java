package cn.rongcloud.mic.room.message;

import cn.rongcloud.common.im.BaseMessage;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/6/12
 */
@Data
public class ChrmSysMessage extends BaseMessage {

    private Integer type; //0 用户被踢出房间

    private String operatorId;

    private String operatorName;

    private String roomId;

    @Override
    public String getObjectName() {
        return "RCMic:chrmSysMsg";
    }
}
