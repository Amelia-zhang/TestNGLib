package com.wx.hashkey;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.wx.hashkey.util.HttpRequest;

public class TestTries {

	String triesUrl = "https://qa-api.hub.hashkey.com/passport/login/tries";
	String username;

	@Test(dataProvider = "testTries")
	public void checkLogin(String phone, String reponseMessage) {
		String getString = HttpRequest.sendGet(triesUrl, "username=" + phone);
		try {
			JSONObject jsonObject = new JSONObject(getString);
			String returnMessage = jsonObject.getString("message");
			Assert.assertEquals(reponseMessage, returnMessage);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@DataProvider(name = "testTries")
	public Object[][] provideObject() {
		return new Object[][] { { "18616771581", "SUCCESS" } };
	}

}
