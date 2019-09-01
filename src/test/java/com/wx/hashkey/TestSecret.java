package com.wx.hashkey;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.wx.hashkey.util.HttpRequest;
import com.wx.hashkey.util.Tokens;

public class TestSecret {
	String secretUrl = "https://qa-api.hub.hashkey.com/system/secretKey/create";
	@Test(dataProvider = "testSecret",dependsOnGroups = "login",groups = "secret")
	public void checkCreateSecretResult(String deviceId, String platFormId, String deviceInfo,
			String reponseMessage) {
		
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("deviceId", deviceId);
			jsonObject.put("platformId", platFormId);
			jsonObject.put("deviceInfo", deviceInfo);
			
			String sendPost = HttpRequest.sendPost(secretUrl, jsonObject.toString());
			//String需要转成json格式来处理
			JSONObject secretJsonObject = new JSONObject(sendPost);
			//System.out.println(secretJsonObject);
			// 获取secret存储备用
			String secret = secretJsonObject.getString("body");
			
			Tokens.saveSecret(secret);

			//System.out.println("secret接口的返回为："+sendPost);
			String returnMessage = secretJsonObject.getString("message");
			Assert.assertEquals(returnMessage, reponseMessage);
			//System.out.println("secret="+secret);
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@DataProvider(name = "testSecret")
	public Object[][] provideObject() {
		return new Object[][] { { "67C05B1B-711A-441F-A62C-5EABB74338B3", "1", "iPhone 6", "SUCCESS" } };
	}
}
