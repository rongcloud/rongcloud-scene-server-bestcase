package cn.rongcloud.mic.shumei.message;

import cn.rongcloud.common.im.BaseMessage;
import lombok.Data;

/**
 * 数美审核 冻结用户信息
 */
@Data
public class ShumeiAuditFreezeMessage extends BaseMessage {

    private String userId;

    private String message;

    private Integer status;

    @Override
    public String getObjectName() {
        return "RCMic:shumeiAuditFreezeMsg";
    }


}
