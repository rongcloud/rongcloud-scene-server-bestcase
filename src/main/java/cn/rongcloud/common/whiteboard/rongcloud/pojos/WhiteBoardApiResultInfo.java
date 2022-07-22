package cn.rongcloud.common.whiteboard.rongcloud.pojos;

import lombok.Data;

/**
 * Created by weiqinxiao on 2019/3/7.
 */
@Data
public class WhiteBoardApiResultInfo {
    private int code;
    private String msg;
    private String data;

    public boolean isSuccess() {
        return code == 200;
    }
}
