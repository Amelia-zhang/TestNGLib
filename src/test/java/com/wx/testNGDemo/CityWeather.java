package com.wx.testNGDemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;

public class CityWeather {
	private String url="";
	
	public String getResponse(String cityCode) {
		String response="";
		String line="";
		url="http://www.weather.com.cn/data/cityinfo/" + cityCode + ".html";
		try {
			HttpURLConnection connection = Connection.getConnection(url);
			connection.connect();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line=bufferedReader.readLine())!=null) {
				response+=line.toString();
				
			}
			bufferedReader.close();
			connection.disconnect();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}
	
	
	

}
