package cn.rongcloud.common.im.pojos;

import lombok.Data;

@Data
public class RTCRecordResp {
    private int code;
    private String errorMessage;
    private String recordId;
}
