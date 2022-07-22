package cn.rongcloud.mic.community.message;

import cn.rongcloud.common.im.BaseMessage;
import lombok.Data;

@Data
public class CommunityDeleteMessage extends BaseMessage {

    //消息内容
    private String communityUid;

    private String fromUserId;

    @Override
    public String getObjectName() {
        return "RCMic:CommunityDelete";
    }

}
