package com.wx.hashkey.util;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密解密工具 加密资金密码的工具类
 * 
 */
public class AESUtils {

	/**
	 * AES加密
	 * 
	 * @param data 将要加密的内容
	 * @param key  密钥
	 * @return 已经加密的内容
	 */
	public static byte[] encrypt(byte[] data, byte[] key) {
		// 不足16字节，补齐内容为差值
		int len = 16 - data.length % 16;
		for (int i = 0; i < len; i++) {
			byte[] bytes = { (byte) len };
			data = ArrayUtils.concat(data, bytes);
		}
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new byte[] {};
	}

	// aes加密
	public static String encryp(String key, String accessToken) {
//		String key = "123456";
//		String content = "bfc353dc22ec4f878f6dc88e6e7c4ae4";
		String value = accessToken.substring(0, 16);
		byte[] encrypt = AESUtils.encrypt(key.getBytes(), value.getBytes());
		byte[] encode = Base64.getEncoder().encode(encrypt);
		String base64 = new String(encode);
		//System.out.println(base64);
		return base64;
	}

}
