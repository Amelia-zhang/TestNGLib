package com.wx.hashkey.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Base64;

//加密密码的工具类
public class EncrypUtils {
	// base64,sha256加密
	public static String encryp(String pwd) {
		byte[] encode = Base64.getEncoder().encode(pwd.getBytes());
		String base64 = new String(encode);
		String sha256 = EncrypUtils.getSHA256(base64 + "hash");
		return sha256;
	}

	/**
	 * 利用java原生的类实现SHA256加密
	 * 
	 * @param str 加密后的报文
	 * @return
	 */
	public static String getSHA256(String str) {
		MessageDigest messageDigest;
		String encodestr = "";
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(str.getBytes("UTF-8"));
			encodestr = byte2Hex(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodestr;
	}

	/**
	 * 将byte转为16进制
	 * 
	 * @param bytes
	 * @return
	 */
	private static String byte2Hex(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length() == 1) {
				// 1得到一位的进行补0操作
				stringBuffer.append("0");
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}

	// aes加密

//	public static void main(String[] args) {
//		String key = "123456";
//		String content = "bfc353dc22ec4f878f6dc88e6e7c4ae4";
//		String substring = content.substring(0, 1);
//		byte[] encrypt = AESUtils.encrypt(key.getBytes(), substring.getBytes());
//		byte[] encode = Base64.getEncoder().encode(encrypt);
//		String base64 = new String(encode);
//		System.out.println(base64);
//	}

}