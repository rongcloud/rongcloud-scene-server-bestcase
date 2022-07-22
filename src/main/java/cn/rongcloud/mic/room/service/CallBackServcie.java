package cn.rongcloud.mic.room.service;

import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.room.pojos.callback.AuditCallBackDto;

public interface CallBackServcie {

    RestResult auditSync(AuditCallBackDto auditCallBackDto);

}
