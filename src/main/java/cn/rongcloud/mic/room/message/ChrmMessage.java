package cn.rongcloud.mic.room.message;

import cn.rongcloud.common.im.BaseMessage;
import java.util.List;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/6/3
 */
@Data
public class ChrmMessage extends BaseMessage {

    // 0: 禁言 1 解除禁言
    private  int type;

    //操作者id
    private String operatorId;

    //操作名称
    private String operatorName;

    //目标用户列表
    private List<TargetUser> targetUsers;

    @Data
    public static class TargetUser{
        //目标用户id
        private String targetUserId;

    }

    @Override
    public String getObjectName() {
        return "RCMic:chrmMsg";
    }
}
