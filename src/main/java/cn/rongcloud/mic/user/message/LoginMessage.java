package cn.rongcloud.mic.user.message;

import cn.rongcloud.common.im.BaseMessage;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/6/3
 */
@Data
public class LoginMessage extends BaseMessage {


    //操作名称
    private String operation;

    //操作者id
    private String userId;

    //操作内容
    private String data;

    @Override
    public String getObjectName() {
        return "RC:ProfileNtf";
    }
}
