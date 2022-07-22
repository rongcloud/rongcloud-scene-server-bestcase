package cn.rongcloud.common.im.pojos;

import java.util.List;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/6/25
 */
@Data
public class IMChrmWhiteListResult {
    // 返回码，200 为正常
    Integer code;

    //消息类型数组。
    List<String> whitlistMsgType;

    public boolean isSuccess() {
        return code == 200;
    }

}
