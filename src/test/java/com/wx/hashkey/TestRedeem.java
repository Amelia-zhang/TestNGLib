package com.wx.hashkey;

import java.text.DecimalFormat;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.wx.hashkey.util.AESUtils;
import com.wx.hashkey.util.HttpRequest;
import com.wx.hashkey.util.Tokens;

import junit.framework.Assert;

@Test(groups = "redeem",dependsOnGroups = "mining")
public class TestRedeem {
	static double WalletAllQtum;
	static double WalletAvaQtum;
	static double WalletFrozenQtum;
	static double WealthAllQtum;
	static double WealthMiningQtum;
	static double WealthRedeemQtum;

	static double QtumRedeemAmount;

	static double WalletAllQtumAgain;
	static double WalletAvaQtumAgain;
	static double WalletFrozenQtumAgain;
	static double WealthAllQtumAgain;
	static double WealthMiningQtumAgain;
	static double WealthRedeemQtumAgain;
	
	static String validateBodyResult;
	static String accessToken;
	static String verifyBodyResult;
	//static double redeemAmount;

	//获得QTUM钱包和财富资产
	String assetUrl = "https://qa-api.hub.hashkey.com/coin/v2/account/asset/QTUM";
	String unit;
	//格式化数字
	DecimalFormat df=new DecimalFormat("#.00000000");
	
	@Test(dependsOnGroups = "secret", dataProvider = "testRedeemAsset", groups = "redeemAsset")
	public void getQtumAsset(String unit, String response) {
		
			String assetResult = HttpRequest.sendGet(assetUrl, "unit=" + unit);
			try {
				JSONObject jsonAssetResult = new JSONObject(assetResult);
				String returnResult = jsonAssetResult.getString("message");
				Assert.assertEquals(response, returnResult);
				
				// 获得钱包账户的全部，可用，冻结
				WalletAllQtum = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets").getJSONObject("WALLET")
						.getDouble("amount")));
				WalletAvaQtum = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets").getJSONObject("WALLET")
						.getDouble("available")));
				WalletFrozenQtum = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets").getJSONObject("WALLET")
						.getDouble("frozen")));
				// 获得财富账户的全部，挖矿中，赎回中
				WealthAllQtum =Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets").getJSONObject("WEALTH")
						.getDouble("amount")));
				WealthMiningQtum = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets").getJSONObject("WEALTH")
						.getDouble("available")));
				WealthRedeemQtum = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets").getJSONObject("WEALTH")
						.getDouble("frozen")));
				
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		



	@DataProvider(name = "testRedeemAsset")
	public Object[][] provideObject() {
		return new Object[][] { { "USD", "SUCCESS" } };
	}
	
	//QTUM输入赎回数量
	String redeemValidateUrl = "https://qa-api.hub.hashkey.com/coin/asset/redeem/validate";
	@Test(dependsOnGroups = "redeemAsset", dataProvider = "provideQtumRedeemQuantity", groups = "redeemValidate")
	public void redeemValidate(String coinCode,String qty,boolean isUrgent,String responseMessageResult) {
		if(WealthMiningQtum>=1) {	
		JSONObject jsonObject=new JSONObject();
			try {
				jsonObject.put("coinCode", coinCode);
				jsonObject.put("qty", qty);
				jsonObject.put("isUrgent", isUrgent);
				String redeemValidateResult = HttpRequest.sendPost(redeemValidateUrl, jsonObject.toString());
				JSONObject redeemValidateJsonObject = new JSONObject(redeemValidateResult);
				String actualMessageResult = redeemValidateJsonObject.getString("message");
				validateBodyResult = redeemValidateJsonObject.getString("body");
				Assert.assertEquals(responseMessageResult, actualMessageResult);
				
				System.out.println("validateBodyResult="+validateBodyResult);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.out.println("普通赎回资产不够了，财富挖矿中资产为"+WealthMiningQtum);
			return;
		}
			
			
		
	}
	
	//普通赎回的数量为1
	@DataProvider(name="provideQtumRedeemQuantity")
	public Object[][] provideRedeemValidate() {
		return new Object[][] { { "QTUM", "1" ,false,"SUCCESS"} };
	}
	
	//校验资金密码
	@Test(dependsOnGroups = "redeemValidate", dataProvider = "providePassword", groups = "redeemVerifyPassword")
	public void redeemVerifyPassword(String encryPassword,String responseMessageResult) {
		if(WealthMiningQtum>=1) {
		//如果写在方法外面，怎样得到上面一个方法的值validateBodyResult
		String verifyPasswordUrl = "https://qa-api.hub.hashkey.com/coin/asset/redeem/password/verify/"+validateBodyResult;
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("password", encryPassword);
			String redeemVerifyPasswordResult = HttpRequest.sendPost(verifyPasswordUrl, jsonObject.toString());
			JSONObject redeemVerifyPasswordJsonObject = new JSONObject(redeemVerifyPasswordResult);
			String actualMessageResult = redeemVerifyPasswordJsonObject.getString("message");
			verifyBodyResult = redeemVerifyPasswordJsonObject.getString("body");
			//QtumRedeemAmount = redeemVerifyPasswordJsonObject.getJSONObject("body").getDouble("amount");
			Assert.assertEquals(responseMessageResult, actualMessageResult);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		}else{
			System.out.println("普通赎回资产不够了，财富挖矿中资产为"+WealthMiningQtum);
			return;
		}
		
	}
	
	//提供加密后的资金密码
	@DataProvider(name="providePassword")
	public Object[][] providePassword() {
		String key = "123456";
		Tokens tokens = Tokens.getLocalData();
		if(tokens!=null  && tokens.accessToken!=null) {
			accessToken = tokens.accessToken;
		}
		String encryPassword = AESUtils.encryp(key, accessToken);
		return new Object[][] { { encryPassword,"SUCCESS"} };
	}
	
	
	//返回赎回结果
	@Test(dependsOnGroups = "redeemVerifyPassword",dataProvider = "provideRedeemResult", groups = "returnRedeemResult")
	public void checkRedeemResult(String responseMessageResult,String responseStatusResult) {
		if(WealthMiningQtum>=1) {
		
		//如果写在方法外面，怎样得到上面一个方法的值validateBodyResult
		String redeemTransferUrl = "https://qa-api.hub.hashkey.com/coin/asset/redeem/transfer/"+verifyBodyResult;
		JSONObject jsonObject=new JSONObject();
		try {
			
			String redeemTransferResult = HttpRequest.sendPost(redeemTransferUrl, jsonObject.toString());
			JSONObject redeemVerifyPasswordJsonObject = new JSONObject(redeemTransferResult);
			String actualMessageResult = redeemVerifyPasswordJsonObject.getString("message");
			String status = redeemVerifyPasswordJsonObject.getJSONObject("body").getString("status");
			QtumRedeemAmount = Double.valueOf(df.format(redeemVerifyPasswordJsonObject.getJSONObject("body").getDouble("amount")));
			
			Assert.assertEquals(responseMessageResult, actualMessageResult);
			Assert.assertEquals(responseStatusResult, status);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		}else{
			System.out.println("普通赎回资产不够了，财富挖矿中资产为"+WealthMiningQtum);
			return;
		}
	}
	
	//赎回返回结果校验
	@DataProvider(name="provideRedeemResult")
	public Object[][] provideRedeemResult() {
		
		return new Object[][] { { "SUCCESS","处理中"} };
	}
	
	//再次校验QTUM的各个账户的资产
	//String assetUrl = "https://qa-api.hub.hashkey.com/coin/v2/account/asset/QTUM";
	//String unit;

	@Test(dependsOnGroups = "returnRedeemResult", dataProvider = "testRedeemAsset")
	public void getQtumAssetAgain(String unit, String response) {
		String assetResult = HttpRequest.sendGet(assetUrl, "unit=" + unit);
		try {
			JSONObject jsonAssetResult = new JSONObject(assetResult);
			String returnResult = jsonAssetResult.getString("message");
			Assert.assertEquals(response, returnResult);
			// 获得钱包账户的全部，可用，冻结
			WalletAllQtumAgain = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets").getJSONObject("WALLET")
					.getDouble("amount")));
			WalletAvaQtumAgain = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets").getJSONObject("WALLET")
					.getDouble("available")));
			WalletFrozenQtumAgain = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets").getJSONObject("WALLET")
					.getDouble("frozen")));
			// 获得财富账户的全部，挖矿中，赎回中
			WealthAllQtumAgain = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets").getJSONObject("WEALTH")
					.getDouble("amount")));
			WealthMiningQtumAgain = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets").getJSONObject("WEALTH")
					.getDouble("available")));
			WealthRedeemQtumAgain = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets").getJSONObject("WEALTH")
					.getDouble("frozen")));
			System.out.println("原来钱包全部"+WalletAllQtum);
			System.out.println("原来钱包可用"+WalletAvaQtum);
			System.out.println("原来钱包冻结"+WalletFrozenQtum);
			System.out.println("原来财富全部"+WealthAllQtum);
			System.out.println("原来财富可用"+WealthMiningQtum);
			System.out.println("原来财富冻结"+WealthRedeemQtum);
			
			System.out.println("现在钱包全部"+WalletAllQtumAgain);
			System.out.println("现在钱包可用"+WalletAvaQtumAgain);
			System.out.println("现在钱包冻结"+WalletFrozenQtumAgain);
			System.out.println("现在财富全部"+WealthAllQtumAgain);
			System.out.println("现在财富可用"+WealthMiningQtumAgain);
			System.out.println("现在财富冻结"+WealthRedeemQtumAgain);
			
			//原来账户的资产和现在账户资产比对
			Assert.assertEquals(WalletAllQtum, WalletAllQtumAgain);
			Assert.assertEquals(WalletAvaQtum, WalletAvaQtumAgain);
			Assert.assertEquals(WalletFrozenQtum, WalletFrozenQtumAgain);
			Assert.assertEquals(WealthAllQtum, WealthAllQtumAgain);
			Assert.assertEquals(WealthMiningQtum-QtumRedeemAmount, WealthMiningQtumAgain);
			Assert.assertEquals(WealthRedeemQtum+QtumRedeemAmount, WealthRedeemQtumAgain);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
}
