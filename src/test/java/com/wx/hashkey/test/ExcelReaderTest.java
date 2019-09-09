package com.wx.hashkey.test;

import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.wx.hashkey.util.AESUtils;
import com.wx.hashkey.util.ExcelReader;
import com.wx.hashkey.util.HttpRequest;
import com.wx.hashkey.util.Tokens;

public class ExcelReaderTest {
	@Test(dataProvider = "test")
	public void finalTestResult(HashMap<String, String> data) {
		String loginUrl = Config.HOST + data.get("Url");
		String requestObj = data.get("Data");
		String expectedMessage = data.get("expectedMessage");
		String method = data.get("Method");
		String name = data.get("Name");
		String paramsConfig = data.get("paramsConfig");
		String passwordConfig = data.get("passwordConfig");
		
		String saveConfigTag=null;
		String useConfigTag =null;
	
		if(paramsConfig!=null&&paramsConfig.length()>0) {
			String[] params = paramsConfig.split(":");
			if(params[0].equals("save")) {
				saveConfigTag = params[1];
				
			}else {
				useConfigTag = params[1];
			}
		}
		
		if(passwordConfig!=null&&passwordConfig.length()>0) {
			Tokens tokens = Tokens.getLocalData();
			String encrypPassword = AESUtils.encryp(passwordConfig,tokens.accessToken);
			requestObj="{\"password\":\""+encrypPassword+"\"}";
		}
		
		String sendPost = "";
		if(useConfigTag!=null && useConfigTag.length()>0) {
			loginUrl+=Tokens.getPreParams();
		}
		
		if (method.equals("post")) {
			sendPost = HttpRequest.sendPost(loginUrl, requestObj);
		} else {
			sendPost = HttpRequest.sendGet(loginUrl, requestObj);
		}

		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(sendPost);
			String returnMessage = jsonObject.getString("message");
			if (sendPost.contains("accessToken")) {
				String accessToken = jsonObject.getJSONObject("body").getString("accessToken");
				Tokens.saveToken(accessToken);
			}
			if(jsonObject.has("body")) {
				if (jsonObject.get("body") instanceof String) {
					if(saveConfigTag!=null && saveConfigTag.length()>0) {
						Tokens.savePreParams(jsonObject.getString("body"));
					}else {
						String secret = jsonObject.getString("body");
						Tokens.saveSecret(secret);
					}
					
				}
			}
			
			
			
			Assert.assertEquals(returnMessage, expectedMessage);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@DataProvider(name = "test")
	public Object[][] provideData() throws IOException, InvalidFormatException {
		// 获取Excel文件的测试数据
		ExcelReader e = new ExcelReader("testdata", "test");
		return e.excelReader2();

	}

}
