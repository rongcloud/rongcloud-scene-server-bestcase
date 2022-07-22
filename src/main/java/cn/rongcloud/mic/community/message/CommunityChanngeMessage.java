package cn.rongcloud.mic.community.message;

import cn.rongcloud.common.im.BaseMessage;
import lombok.Data;

import java.util.List;

@Data
public class CommunityChanngeMessage  extends BaseMessage {

    //社区uid
    private String communityUid;

    private String fromUserId;

    private String message;

    private List<String> channelUids;

    @Override
    public String getObjectName() {
        return "RCMic:CommunityChange";
    }
}
