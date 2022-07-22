package cn.rongcloud.mic.community.message;

import cn.rongcloud.common.im.BaseMessage;
import lombok.Data;

@Data
public class UserUpdateMessage  extends BaseMessage {

    private String portrait;

    private String nickName;

    private String userId;

    private Integer type;

    @Override
    public String getObjectName() {
        return "RCMic:UserUpdate";
    }

}
