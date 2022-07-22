package cn.rongcloud.common.im.pojos;

import java.util.List;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/6/25
 */
@Data
public class IMChatRoomUserResult {
    // 返回码，200 为正常.如果您正在使用开发环境的 AppKey，您的应用只能注册 100 名用户，达到上限后，将返回错误码 2007.如果您需要更多的测试账户数量，您需要在应用配置中申请“增加测试人数”。
    Integer code;

    // 当前聊天室中用户数
    Long total;

    // 聊天室成员数组，最多为 500 个。
    List<IMRoomUserInfo> users;

    // 用户 Id
    String id;

    //加入聊天室时间
    String time;

    public boolean isSuccess() {
        return code == 200;
    }

}
