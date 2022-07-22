package cn.rongcloud.mic.common.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * @author zhangxuesong
 * @create 09 08, 2020
 * @since 1.0.0
 */
public class AESSecretUtil {
    /**秘钥的大小*/
    private static final int KEYSIZE = 128;

    public static final String AES="AES";

    public static final String SHA1PRNG="SHA1PRNG";

    /**
     * AES加密
     * @param data
     * @param key
     * @return
     */
    public static byte[] encrypt(String data, String key) {
        if(StringUtils.isNotBlank(data)){
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
                //选择一种固定算法，为了避免不同java实现的不同算法，生成不同的密钥，而导致解密失败
                SecureRandom random = SecureRandom.getInstance(SHA1PRNG);
                random.setSeed(key.getBytes());
                keyGenerator.init(KEYSIZE, random);
                SecretKey secretKey = keyGenerator.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, AES);
                Cipher cipher = Cipher.getInstance(AES);// 创建密码器
                byte[] byteContent = data.getBytes("utf-8");
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);// 初始化
                byte[] result = cipher.doFinal(byteContent);
                return result; // 加密
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * AES加密，返回String
     * @param data
     * @param key
     * @return
     */
    public static String encryptToStr(String data, String key){

        return StringUtils.isNotBlank(data)?parseByte2HexStr(encrypt(data, key)):null;
    }


    /**
     * AES解密
     * @param data
     * @param key
     * @return
     */
    public static byte[] decrypt(byte[] data, String key) {
        if (ArrayUtils.isNotEmpty(data)) {
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
                //选择一种固定算法，为了避免不同java实现的不同算法，生成不同的密钥，而导致解密失败
                SecureRandom random = SecureRandom.getInstance(SHA1PRNG);
                random.setSeed(key.getBytes());
                keyGenerator.init(KEYSIZE, random);
                SecretKey secretKey = keyGenerator.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, AES);
                Cipher cipher = Cipher.getInstance(AES);// 创建密码器
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);// 初始化
                byte[] result = cipher.doFinal(data);
                return result; // 加密
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * AES解密，返回String
     * @param enCryptdata
     * @param key
     * @return
     */
    public static String decryptToStr(String enCryptdata, String key) {
        return StringUtils.isNotBlank(enCryptdata)?new String(decrypt(parseHexStr2Byte(enCryptdata), key)):null;
    }

    /**
     * 将二进制转换成16进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

}