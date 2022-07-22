package cn.rongcloud.common.utils;

import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *@Author gaoxin
 *@Date 2021/9/8 5:18 下午
 *@Version 1.0
 **/

public class WXUtils {

	/**
	 * @Description:解密敏感信息
	 * @Param: [encryptedData, sessionKey, iv]
	 * @return: com.alibaba.fastjson.JSONObject
	 * @Author: gaoxin
	 * @Date: 2021/9/8
	 */
	public static JSONObject getDecodeInfo(String encryptedData, String sessionKey, String iv){


		// 被加密的数据
		byte[] dataByte = Base64.decode(encryptedData);

		// 加密秘钥
		byte[] keyByte = Base64.decode(sessionKey);

		// 偏移量
		byte[] ivByte = Base64.decode(iv);
		
		try {
			// 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
			int base = 16;

			if (keyByte.length % base != 0) {

				int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);

				byte[] temp = new byte[groups * base];

				Arrays.fill(temp, (byte) 0);

				System.arraycopy(keyByte, 0, temp, 0, keyByte.length);

				keyByte = temp;

			}

			// 初始化
			Security.addProvider(new BouncyCastleProvider());

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");

			SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");

			AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");

			parameters.init(new IvParameterSpec(ivByte));

			cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化

			byte[] resultByte = cipher.doFinal(dataByte);

			if (null != resultByte && resultByte.length > 0) {

				String result = new String(resultByte, "UTF-8");

				return JSONObject.parseObject(result);

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}



}
