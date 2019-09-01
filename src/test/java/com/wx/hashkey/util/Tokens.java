package com.wx.hashkey.util;

import com.google.gson.Gson;

public class Tokens {

	public String accessToken;

	public String secret;

	public static String getSign(String urlPath) {
		Tokens tokens = getLocalData();
		String result = "";
		if (tokens != null) {

			if (tokens.accessToken != null) {
				result += tokens.accessToken;
			}
			if (tokens.secret != null) {
				result += tokens.secret;
			}
			String url = URLUtils.getUrl(urlPath);
			result += url;

			return EncrypUtils.getSHA256(result);
		}

		return null;
	}

	public static void saveSecret(String secret) {
		Tokens tokens = getLocalData();
		if (tokens == null) {
			tokens = new Tokens();
		}
		tokens.secret = secret;
		saveToLocal(tokens);
	}

	public static void saveToken(String token) {
		Tokens tokens = getLocalData();
		if (tokens == null) {
			tokens = new Tokens();
		}
		tokens.accessToken = token;
		saveToLocal(tokens);
	}

	public static Tokens getLocalData() {
		String localData = FileHelper.readFile();

		if (localData != null && localData.length() > 0) {
			Tokens tokens = new Gson().fromJson(localData, Tokens.class);

			return tokens;
		}

		return null;
	}

	public static void saveToLocal(Tokens tokens) {
		if (tokens == null) {
			return;
		}
		String localStr = new Gson().toJson(tokens);

		FileHelper.writeFile(localStr);

	}

}
