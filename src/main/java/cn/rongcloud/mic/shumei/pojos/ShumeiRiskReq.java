package cn.rongcloud.mic.shumei.pojos;

import lombok.Data;

@Data
public class ShumeiRiskReq {


    private String tokenId;

    private String ip;

    private long timestamp;

    private String deviceId;

    private Integer isTokenSeperate;

    private String phone;

    private String countryCode;

    private String os;

    private String  appVersion;


}
