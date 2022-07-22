package cn.rongcloud.common.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang.RandomStringUtils;

/**
 * Created by sunyinglong on 2020/6/25
 */
public class SecurityUtils {
	private SecurityUtils() {}
	public enum HmacAlgorithm {
		HMAC_MD5("HmacMD5"), HMAC_SHA1("HmacSHA1"), HMAC_SHA256("HmacSHA256"), HMAC_SHA384("HmacSHA384"), HMAC_SHA512(
				"HmacSHA512");

		private String algorithm;

		private HmacAlgorithm(String algorithm) {
			this.algorithm = algorithm;
		}

		public String getAlgorithm() {
			return algorithm;
		}
	}
	//AES加密方法
	public static final String AES_ECB_PKCS7 = "AES/ECB/PKCS7Padding";
    public static final String AES_ECB_PKCS5 = "AES/ECB/PKCS5Padding";
    public static final String AES_CBC_PKCS7 = "AES/CBC/PKCS7Padding";
    public static final String AES_CBC_PKCS5 = "AES/CBC/PKCS5Padding";

	public static byte[] encryptHMAC(String secret, String data, HmacAlgorithm algorithm) throws IOException {
		return encryptHMAC(secret.getBytes(StandardCharsets.UTF_8), data.getBytes(
			StandardCharsets.UTF_8), algorithm);
	}

	public static byte[] encryptHMAC(byte[] secret, byte[] data, HmacAlgorithm algorithm) throws IOException {
		byte[] bytes = null;
		try {
			SecretKey secretKey = new SecretKeySpec(secret, algorithm.getAlgorithm());
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			bytes = mac.doFinal(data);
		} catch (GeneralSecurityException gse) {
			throw new IOException(gse.toString());
		}
		return bytes;
	}

	public static String byte2hex(byte[] bytes) {
		StringBuilder sign = new StringBuilder();
		for (byte aByte : bytes) {
			String hex = Integer.toHexString(aByte & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex);
		}
		return sign.toString();
	}

	public static String generateSalt(int length) {
		return RandomStringUtils.random(length);
	}

	public static byte[] encryptWithAES128ECB(byte[] in, String key) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		if (key == null || key.length() != 16) {
			throw new InvalidParameterException("key is invalid");
		}

		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
		Cipher cipher = Cipher.getInstance(AES_ECB_PKCS5);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		return cipher.doFinal(in);
	}
}
