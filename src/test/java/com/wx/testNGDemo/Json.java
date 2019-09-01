package com.wx.testNGDemo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

//根据json中的key返回对应的value
public class Json {
	public static String getCityName(String jsonString) {
		String jsonValue = "";
		if (jsonString == null || jsonString.trim().length() < 1) {
			return null;
		} else {
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				jsonValue = jsonObject.getJSONObject("weatherinfo")
						.getString("city");
				
//			Gson gson=new Gson();
//			JsonObject object = gson.fromJson(jsonString, JsonObject.class);
//
//			jsonValue=object.getAsJsonObject("weatherinfo").get("city").getAsString();
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonValue;

		}

	} 
	
	//base64,sha256加密
	
	
	
}
