package com.wx.hashkey;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.wx.hashkey.util.HttpRequest;
import com.wx.hashkey.util.Tokens;

import junit.framework.Assert;

@Test(groups = "mining")
public class TestMining {
	static double WalletAllQTUM;
	static double WalletAvaQTUM;
	static double WalletFrozenQTUM;
	static double WealthAllQTUM;
	static double WealthMiningQTUM;
	static double WealthRedeemQTUM;
	
	static double QtumMiningsAmount;
	
	static double WalletAllQTUMAgain;
	static double WalletAvaQTUMAgain;
	static double WalletFrozenQTUMAgain;
	static double WealthAllQTUMAgain;
	static double WealthMiningQTUMAgain;
	static double WealthRedeemQTUMAgain;
	//获得QTUM钱包和财富资产
	String assetUrl="https://qa-api.hub.hashkey.com/coin/v2/account/asset/QTUM";
	String unit;
	@Test(dependsOnGroups = "secret",dataProvider = "testAsset",groups = "asset")
	public void getQtumAsset(String unit,String response) {
		
		String assetResult= HttpRequest.sendGet(assetUrl, "unit="+unit);
		try {
			JSONObject jsonAssetResult = new JSONObject(assetResult);
			String returnResult = jsonAssetResult.getString("message");
			Assert.assertEquals(response, returnResult);
			//获得钱包账户的全部，可用，冻结
			WalletAllQTUM = jsonAssetResult.getJSONObject("body").getJSONObject("assets").
			getJSONObject("WALLET").getDouble("amount");
			WalletAvaQTUM = jsonAssetResult.getJSONObject("body").getJSONObject("assets").
					getJSONObject("WALLET").getDouble("available");
			WalletFrozenQTUM = jsonAssetResult.getJSONObject("body").getJSONObject("assets").
					getJSONObject("WALLET").getDouble("frozen");
			//获得财富账户的全部，挖矿中，赎回中
			WealthAllQTUM = jsonAssetResult.getJSONObject("body").getJSONObject("assets").
					getJSONObject("WEALTH").getDouble("amount");
			WealthMiningQTUM = jsonAssetResult.getJSONObject("body").getJSONObject("assets").
					getJSONObject("WEALTH").getDouble("available");
			WealthRedeemQTUM = jsonAssetResult.getJSONObject("body").getJSONObject("assets").
					getJSONObject("WEALTH").getDouble("frozen");
				
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@DataProvider(name="testAsset")
	public Object[][] provideObject() {
		return new Object[][] { { "USD", "SUCCESS" } };
	}
	

	//Qtum加入挖矿
//	String qtumMining="https://qa-api.hub.hashkey.com/coin/asset/mining";
//	@Test(dataProvider = "testQtumMinings",dependsOnGroups = "asset",groups = "QtumMinings")
//	public void joinQtumMinings(String coinCode,String qty,String responseMessageResult) {
//		if(WalletAvaQTUM>=1) {
//			JSONObject jsonObject = new JSONObject();
//			try {
//				jsonObject.put("coinCode", coinCode);
//				jsonObject.put("qty", qty);
//				String QtumPostResult = HttpRequest.sendPost(qtumMining, jsonObject.toString());
//				JSONObject QtumJSONObject = new JSONObject(QtumPostResult);
//				
//				//判断
//				String actualResponseResult = QtumJSONObject.getString("message");
//				Assert.assertEquals(actualResponseResult, responseMessageResult);
//				QtumMiningsAmount = QtumJSONObject.getJSONObject("body").getDouble("amount");
//				
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}else {
//			System.out.println("挖矿资产不够了，钱包可用资产为"+WalletAvaQTUM);
//			return;
//		}
//		
//		
//	}
//	
//	
//	//加入挖矿的数量为1
//	@DataProvider(name="testQtumMinings")
//	public Object[][] provideQtumData() {
//		return new Object[][] { { "QTUM", "1" ,"SUCCESS"} };
//	}
//	
//	//再次获得QTUM钱包和财富的资产
//	String qtumAssetUrl="https://qa-api.hub.hashkey.com/coin/v2/account/asset/QTUM";
//	String qtumUnit;
//	@Test(dependsOnGroups = "QtumMinings",dataProvider = "testQtumAsset",groups = "qtumAsset")
//	public void getQtumAssetAgain(String qtumUnit,String expectResponse,double QtumMiningsAmount) {
//		
//		String assetResult= HttpRequest.sendGet(qtumAssetUrl, "unit="+qtumUnit);
//		try {
//			JSONObject jsonAssetResult = new JSONObject(assetResult);
//			String returnResult = jsonAssetResult.getString("message");
//			
//			//再次获得钱包账户的全部，可用，冻结
//			WalletAllQTUMAgain = jsonAssetResult.getJSONObject("body").getJSONObject("assets").
//			getJSONObject("WALLET").getDouble("amount");
//			WalletAvaQTUMAgain = jsonAssetResult.getJSONObject("body").getJSONObject("assets").
//					getJSONObject("WALLET").getDouble("available");
//			WalletFrozenQTUMAgain = jsonAssetResult.getJSONObject("body").getJSONObject("assets").
//					getJSONObject("WALLET").getDouble("frozen");
//			//获得财富账户的全部，挖矿中，赎回中
//			WealthAllQTUMAgain = jsonAssetResult.getJSONObject("body").getJSONObject("assets").
//					getJSONObject("WEALTH").getDouble("amount");
//			WealthMiningQTUMAgain = jsonAssetResult.getJSONObject("body").getJSONObject("assets").
//					getJSONObject("WEALTH").getDouble("available");
//			WealthRedeemQTUMAgain = jsonAssetResult.getJSONObject("body").getJSONObject("assets").
//					getJSONObject("WEALTH").getDouble("frozen");
//			
//			//返回结果判断
//			Assert.assertEquals(expectResponse, returnResult);
//			
//			//钱包资产前后比对
//			Assert.assertEquals(WalletAllQTUM-QtumMiningsAmount, WalletAllQTUMAgain);
//			Assert.assertEquals(WalletAvaQTUM-QtumMiningsAmount, WalletAvaQTUMAgain);
//			Assert.assertEquals(WalletFrozenQTUM, WalletFrozenQTUMAgain);
//			
//			//财富资产前后比对
//			Assert.assertEquals(WealthAllQTUM+QtumMiningsAmount, WealthAllQTUMAgain);
//			Assert.assertEquals(WealthMiningQTUM+QtumMiningsAmount, WealthMiningQTUMAgain);
//			Assert.assertEquals(WealthRedeemQTUM, WealthRedeemQTUMAgain);
//			
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//	}
//	
//	@DataProvider(name="testQtumAsset")
//	public Object[][] provideQtumAssetAgain() {
//		return new Object[][] { { "USD", "SUCCESS",QtumMiningsAmount} };
//	}
//	
//	
	

}
