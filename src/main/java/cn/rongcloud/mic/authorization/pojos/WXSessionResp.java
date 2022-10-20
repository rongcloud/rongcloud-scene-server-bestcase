package cn.rongcloud.mic.authorization.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author qzyu
 * @date 2022/10/17
 */
@Data
public class WXSessionResp {

    @JsonProperty("openid")
    private String openId;

    @JsonProperty("session_key")
    private String sessionKey;

    @JsonProperty("unionid")
    private String unionId;

    @JsonProperty("errcode")
    private Integer errCode;

    @JsonProperty("errmsg")
    private String errMsg;
}
