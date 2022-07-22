package cn.rongcloud.common.sms.pojos;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/6/25
 */
@Data
public class VerificationCodeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sessionId;

    private Date createDt;

}