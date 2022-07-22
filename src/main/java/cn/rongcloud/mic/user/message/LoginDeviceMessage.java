package cn.rongcloud.mic.user.message;

import cn.rongcloud.common.im.BaseMessage;
import lombok.Data;

@Data
public class LoginDeviceMessage extends BaseMessage {


    private String userId;

    private  String platform;


    @Override
    public String getObjectName() {
        return "RCMic:loginDeviceMsg";
    }

}
