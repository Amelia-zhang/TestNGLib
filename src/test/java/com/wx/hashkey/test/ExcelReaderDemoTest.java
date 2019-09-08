package com.wx.hashkey.test;

import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.wx.hashkey.util.ExcelReaderDemo;
import com.wx.hashkey.util.HttpRequest;
import com.wx.hashkey.util.Tokens;

public class ExcelReaderDemoTest {
	@Test(dataProvider = "test")
	public void finalTestResult(HashMap<String, String> data) {
		String loginUrl = Config.HOST + data.get("Url");
		String requestObj = data.get("Data");
		String expectedMessage = data.get("expectedMessage");
		String method = data.get("Method");
		String name = data.get("Name");
		// System.out.println(name);
		String sendPost = "";
		// System.out.println(requestObj.toString());
		if (method.equals("post")) {
			sendPost = HttpRequest.sendPost(loginUrl, requestObj);
		} else {
			sendPost = HttpRequest.sendGet(loginUrl, requestObj);
		}
		// System.out.println(sendPost);

		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(sendPost);
			// System.out.println(jsonObject);
			String returnMessage = jsonObject.getString("message");
			// Object secret = jsonObject.get("body");
			// System.out.println("login接口返回"+sendPost);
			if (sendPost.contains("accessToken")) {
				String accessToken = jsonObject.getJSONObject("body").getString("accessToken");
				Tokens.saveToken(accessToken);
			}
			if (jsonObject.get("body") instanceof String) {
				String secret = jsonObject.getString("body");
				Tokens.saveSecret(secret);
			}
			Assert.assertEquals(returnMessage, expectedMessage);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@DataProvider(name = "test")
	public Object[][] provideData() throws IOException {
		// 获取Excel文件的测试数据
		Object[][] data = null;
		try {
			data = ExcelReaderDemo.excelReader();
			// System.out.println(data);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;

	}

}
