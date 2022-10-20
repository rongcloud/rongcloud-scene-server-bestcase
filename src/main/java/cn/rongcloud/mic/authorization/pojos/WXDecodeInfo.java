package cn.rongcloud.mic.authorization.pojos;

import lombok.Data;

/**
 * @author qzyu
 * @date 2022/10/17
 */
@Data
public class WXDecodeInfo {

    private String phoneNumber;
    private Watermark watermark;
    private String purePhoneNumber;
    private String countryCode;
}
