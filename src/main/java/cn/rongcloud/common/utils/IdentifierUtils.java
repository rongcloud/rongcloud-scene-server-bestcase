package cn.rongcloud.common.utils;

/**
 * Created by sunyinglong on 2020/6/25
 */
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import org.apache.commons.lang.RandomStringUtils;

public class IdentifierUtils {

    private IdentifierUtils() {
        throw new IllegalStateException("IdentifierUtils Utility class");
    }

    public static String random(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static String uuid() {
        return uuid24();
    }

    public static String uuid24() {
        UUID uuid = UUID.randomUUID();
        return base64Encode(uuid.getMostSignificantBits()) + base64Encode(
                uuid.getLeastSignificantBits());
    }

    /**
     * 生成16位不重复的随机数，含数字+大小写
     * @return
     */
    public static String getGUID() {
        StringBuilder sb = new StringBuilder();
        //产生16位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < 16; i++) {
            //产生0-2的3位随机数
            int type = rd.nextInt(3);
            switch (type){
                case 0:
                    //0-9的随机数
                    sb.append(rd.nextInt(10));
                    break;
                case 1:
                    //ASCII在65-90之间为大写,获取大写随机
                    sb.append((char)(rd.nextInt(25)+65));
                    break;
                case 2:
                    //ASCII在97-122之间为小写，获取小写随机
                    sb.append((char)(rd.nextInt(25)+97));
                    break;
                default:
                    break;
            }
        }
        return sb.toString();
    }


    public static String uuid32() {
        return uuid36().replace("-", "");
    }

    public static String uuid36() {
        return UUID.randomUUID().toString();
    }

    private static String base64Encode(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES).putLong(value);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buffer.array());
    }

}