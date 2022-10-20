package cn.rongcloud.mic.authorization.pojos;

import cn.rongcloud.mic.user.pojos.ResLogin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qzyu
 * @date 2022/10/17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResLoginWX {

    private ImTokenInfo imObj;
    private PhoneInfo phoneInfo;
    private String userName;
    
    public static ResLoginWX buildResLoginWX(ResLogin login, WXDecodeInfo decodeInfo) {
        return ResLoginWX.builder()
            .imObj(ImTokenInfo.builder()
                .imToken(login.getImToken())
                .portrait(login.getPortrait())
                .authorization(login.getAuthorization())
                .build())
            .phoneInfo(PhoneInfo.builder()
                .phoneNumber(decodeInfo.getPhoneNumber())
                .purePhoneNumber(decodeInfo.getPurePhoneNumber())
                .countryCode(decodeInfo.getCountryCode())
                .watermark(decodeInfo.getWatermark())
                .build())
            .userName(login.getUserName())
            .build();
    }
}
