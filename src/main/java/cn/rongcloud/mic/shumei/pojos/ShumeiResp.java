package cn.rongcloud.mic.shumei.pojos;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShumeiResp implements Serializable {

    private Integer code;

    private String message;

    private String riskLevel;

    private String riskDescription;
}
