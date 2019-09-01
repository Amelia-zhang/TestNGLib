package com.wx.testNGDemo;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import junit.framework.Assert;

public class Test {
	CityWeather cityWeather = new CityWeather();
	
	@org.testng.annotations.Test(dataProvider = "test")
	public void checkResult(String cityCode, String expectCity) {
		String resultString = cityWeather.getResponse(cityCode);

		System.out.println(resultString);

		String cityString = Json.getCityName(resultString);
		Assert.assertEquals(expectCity, cityString);

	}

//	@org.testng.annotations.Test()
//	public void testShanghai() {
//		checkResult("101020100", "上海");
//	}
//
//	@org.testng.annotations.Test
//	public void testShenzhen() {
//		checkResult("101280601", "深圳");
//	}
//
//	@org.testng.annotations.Test
//	public void testBeijing() {
//		checkResult("101010100", "北京");
//	}

	@DataProvider(name = "test")
	public Object[][] provideObject() {

		return new Object[][] { { "101020100", "上海" }, 
			{ "101280601", "深圳" }, { "101010100", "北京" } };
	}

}
