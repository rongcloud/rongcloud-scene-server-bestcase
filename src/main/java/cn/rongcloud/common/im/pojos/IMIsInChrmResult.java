package cn.rongcloud.common.im.pojos;

import lombok.Data;

/**
 * Created by sunyinglong on 2020/6/25
 */
@Data
public class IMIsInChrmResult {
    // 返回码，200 为正常.
    Integer code;
    Boolean isInChrm;

    public boolean isSuccess() {
        return code == 200;
    }
}
