package cn.rongcloud.mic.community.message;

import cn.rongcloud.common.im.BaseMessage;
import lombok.Data;

@Data
public class CommunityNoticeMessage extends BaseMessage {

    //消息内容
    private String message;

    //社区uid
    private String communityUid;

    private String fromUserId;

    //0:代表可点击（同意或拒绝）1:代表只展示
    private Integer type;

    @Override
    public String getObjectName() {
        return "RCMic:CommunitySysNotice";
    }
}
