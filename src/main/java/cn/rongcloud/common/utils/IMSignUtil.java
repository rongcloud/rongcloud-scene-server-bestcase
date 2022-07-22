package cn.rongcloud.common.utils;

import cn.rongcloud.common.im.config.IMProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sunyinglong on 2020/7/8
 */
@Component
public class IMSignUtil {

    @Autowired
    IMProperties imProperties;

    public static String getNonce() {
        return String.valueOf(Math.random() * 1000000);
    }
    public static String getTimestamp(){
        return String.valueOf(System.currentTimeMillis() / 1000);
    }
    public static String toSign(String appSecret, String nonce, String timestamp){
        return CodeUtil.hexSHA1(appSecret + nonce + timestamp);
    }

    public String toSign(String nonce, String timestamp){
        return CodeUtil.hexSHA1(imProperties.getSecret() + nonce + timestamp);
    }

    public boolean validSign(String nonce, String timestamp, String sign) {
        return sign.equals(toSign(nonce, timestamp));
    }

    public  boolean validSign(String appSecret, String nonce, String timestamp, String sign) {
        return sign.equals(toSign(appSecret, nonce, timestamp));
    }
}
