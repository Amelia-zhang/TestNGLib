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


@Test(groups = "emergencyRedemption",dependsOnGroups = "redeem")
public class TestEmergencyRedemption {

	static double WalletAllQTUM;
	static double WalletAvaQTUM;
	static double WalletFrozenQTUM;
	static double WealthAllQTUM;
	static double WealthMiningQTUM;
	static double WealthRedeemQTUM;

	static double WalletAllQTUMAgain;
	static double WalletAvaQTUMAgain;
	static double WalletFrozenQTUMAgain;
	static double WealthAllQTUMAgain;
	static double WealthMiningQTUMAgain;
	static double WealthRedeemQTUMAgain;

	static String validateBodyResult;
	static String accessToken;
	static String verifyBodyResult;
	static double redeemAmount;
	static double urgentFee;
	// 获得QTUM钱包和财富资产
	String assetUrl = "https://qa-api.hub.hashkey.com/coin/v2/account/asset/QTUM";
	String unit;

	// 格式化数字
	DecimalFormat df = new DecimalFormat("#.00000000");

	@Test(dependsOnGroups = "secret", dataProvider = "testEmergencyRedeemAsset", groups = "emergencyRedeemAsset")
	public void getQtumAsset(String unit, String response) {
		String assetResult = HttpRequest.sendGet(assetUrl, "unit=" + unit);
		try {
			JSONObject jsonAssetResult = new JSONObject(assetResult);
			String returnResult = jsonAssetResult.getString("message");
			Assert.assertEquals(response, returnResult);
			// 获得钱包账户的全部，可用，冻结
			WalletAllQTUM = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets")
					.getJSONObject("WALLET").getDouble("amount")));
			WalletAvaQTUM = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets")
					.getJSONObject("WALLET").getDouble("available")));
			WalletFrozenQTUM = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets")
					.getJSONObject("WALLET").getDouble("frozen")));
			// 获得财富账户的全部，挖矿中，赎回中
			WealthAllQTUM = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets")
					.getJSONObject("WEALTH").getDouble("amount")));
			WealthMiningQTUM = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets")
					.getJSONObject("WEALTH").getDouble("available")));
			WealthRedeemQTUM = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets")
					.getJSONObject("WEALTH").getDouble("frozen")));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@DataProvider(name = "testEmergencyRedeemAsset")
	public Object[][] provideObject() {
		return new Object[][] { { "USD", "SUCCESS" } };
	}

	// 紧急赎回QTUM输入赎回数量
	String redeemValidateUrl = "https://qa-api.hub.hashkey.com/coin/asset/redeem/validate";

	@Test(dependsOnGroups = "emergencyRedeemAsset", dataProvider = "provideQtumEmergencyRedeemQuantity", groups = "emergencyRedeemValidate")
	public void redeemValidate(String coinCode, String qty, boolean isUrgent, String responseMessageResult) {
		if(WealthMiningQTUM>=1) {	
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("coinCode", coinCode);
			jsonObject.put("qty", qty);
			jsonObject.put("isUrgent", isUrgent);
			String redeemValidateResult = HttpRequest.sendPost(redeemValidateUrl, jsonObject.toString());
			JSONObject redeemValidateJsonObject = new JSONObject(redeemValidateResult);
			String actualMessageResult = redeemValidateJsonObject.getString("message");
			validateBodyResult = redeemValidateJsonObject.getString("body");
			Assert.assertEquals(responseMessageResult, actualMessageResult);

			System.out.println("validateBodyResult=" + validateBodyResult);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}else{
			System.out.println("紧急赎回资产不够了，财富挖矿中资产为"+WealthMiningQTUM);
			return;
		}

	}

	// 紧急赎回的数量为1
	@DataProvider(name = "provideQtumEmergencyRedeemQuantity")
	public Object[][] provideRedeemValidate() {
		return new Object[][] { { "QTUM", "1", true, "SUCCESS" } };
	}

	// 校验资金密码
	@Test(dependsOnGroups = "emergencyRedeemValidate", dataProvider = "provideEmergencyPassword", groups = "emergencyRedeemVerifyPassword")
	public void redeemVerifyPassword(String encryPassword, String responseMessageResult) {
		if(WealthMiningQTUM>=1) {	
		// 如果写在方法外面，怎样得到上面一个方法的值validateBodyResult
		String verifyPasswordUrl = "https://qa-api.hub.hashkey.com/coin/asset/redeem/password/verify/"
				+ validateBodyResult;
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("password", encryPassword);
			String redeemVerifyPasswordResult = HttpRequest.sendPost(verifyPasswordUrl, jsonObject.toString());
			JSONObject redeemVerifyPasswordJsonObject = new JSONObject(redeemVerifyPasswordResult);
			String actualMessageResult = redeemVerifyPasswordJsonObject.getString("message");
			verifyBodyResult = redeemVerifyPasswordJsonObject.getString("body");
			Assert.assertEquals(responseMessageResult, actualMessageResult);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}else{
			System.out.println("紧急赎回资产不够了，财富挖矿中资产为"+WealthMiningQTUM);
			return;
		}

	}

	// 提供加密后的资金密码
	@DataProvider(name = "provideEmergencyPassword")
	public Object[][] providePassword() {
		String key = "123456";
		Tokens tokens = Tokens.getLocalData();
		if (tokens != null && tokens.accessToken != null) {
			accessToken = tokens.accessToken;
		}
		String encryPassword = AESUtils.encryp(key, accessToken);
		return new Object[][] { { encryPassword, "SUCCESS" } };
	}

	// 返回赎回结果
	@Test(dependsOnGroups = "emergencyRedeemVerifyPassword", dataProvider = "provideEmergencyRedeemResult", groups = "returnEmergencyRedeemResult")
	public void checkRedeemResult(String responseMessageResult, String responseStatusResult) {
		if(WealthMiningQTUM>=1) {	
		// 如果写在方法外面，怎样得到上面一个方法的值validateBodyResult
		String redeemTransferUrl = "https://qa-api.hub.hashkey.com/coin/asset/redeem/transfer/" + verifyBodyResult;
		JSONObject jsonObject = new JSONObject();
		try {

			String redeemTransferResult = HttpRequest.sendPost(redeemTransferUrl, jsonObject.toString());
			JSONObject redeemVerifyPasswordJsonObject = new JSONObject(redeemTransferResult);
			String actualMessageResult = redeemVerifyPasswordJsonObject.getString("message");
			String status = redeemVerifyPasswordJsonObject.getJSONObject("body").getString("status");
			// 赎回金额
			redeemAmount = Double
					.valueOf(df.format(redeemVerifyPasswordJsonObject.getJSONObject("body").getDouble("amount")));
			// 手续费
			urgentFee = Double
					.valueOf(df.format(redeemVerifyPasswordJsonObject.getJSONObject("body").getDouble("urgentFee")));
			Assert.assertEquals(responseMessageResult, actualMessageResult);
			Assert.assertEquals(responseStatusResult, status);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}else{
			System.out.println("紧急赎回资产不够了，财富挖矿中资产为"+WealthMiningQTUM);
			return;
		}
	}

	// 赎回返回结果校验
	@DataProvider(name = "provideEmergencyRedeemResult")
	public Object[][] provideRedeemResult() {
		return new Object[][] { { "SUCCESS", "成功" } };
	}

	// 再次校验QTUM的各个账户的资产
	// String assetUrl =
	// "https://qa-api.hub.hashkey.com/coin/v2/account/asset/QTUM";
	// String unit;

	@Test(dependsOnGroups = "returnEmergencyRedeemResult", dataProvider = "testEmergencyRedeemAsset")
	public void getQtumAssetAgain(String unit, String response) {
		String assetResult = HttpRequest.sendGet(assetUrl, "unit=" + unit);
		try {
			JSONObject jsonAssetResult = new JSONObject(assetResult);
			String returnResult = jsonAssetResult.getString("message");
			Assert.assertEquals(response, returnResult);
			// 获得钱包账户的全部，可用，冻结
			WalletAllQTUMAgain = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets")
					.getJSONObject("WALLET").getDouble("amount")));
			WalletAvaQTUMAgain = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets")
					.getJSONObject("WALLET").getDouble("available")));
			WalletFrozenQTUMAgain = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body")
					.getJSONObject("assets").getJSONObject("WALLET").getDouble("frozen")));
			// 获得财富账户的全部，挖矿中，赎回中
			WealthAllQTUMAgain = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body").getJSONObject("assets")
					.getJSONObject("WEALTH").getDouble("amount")));
			WealthMiningQTUMAgain = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body")
					.getJSONObject("assets").getJSONObject("WEALTH").getDouble("available")));
			WealthRedeemQTUMAgain = Double.valueOf(df.format(jsonAssetResult.getJSONObject("body")
					.getJSONObject("assets").getJSONObject("WEALTH").getDouble("frozen")));

			System.out.println("原来钱包全部"+WalletAllQTUM);
			System.out.println("原来钱包可用"+WalletAvaQTUM);
			System.out.println("原来钱包冻结"+WalletFrozenQTUM);
			System.out.println("原来财富全部"+WealthAllQTUM);
			System.out.println("原来财富可用"+WealthMiningQTUM);
			System.out.println("原来财富冻结"+WealthRedeemQTUM);
			
			System.out.println("现在钱包全部"+WalletAllQTUMAgain);
			System.out.println("现在钱包可用"+WalletAvaQTUMAgain);
			System.out.println("现在钱包冻结"+WalletFrozenQTUMAgain);
			System.out.println("现在财富全部"+WealthAllQTUMAgain);
			System.out.println("现在财富可用"+WealthMiningQTUMAgain);
			System.out.println("现在财富冻结"+WealthRedeemQTUMAgain);
			
			System.out.println("手续费"+urgentFee);
			System.out.println("紧急赎回金额"+redeemAmount);
			
			
			// 原来账户的资产和现在账户资产比对
			Assert.assertEquals(Double.valueOf(df.format(WalletAllQTUM+redeemAmount)), WalletAllQTUMAgain);
			Assert.assertEquals(Double.valueOf(df.format(WalletAvaQTUM+redeemAmount)), WalletAvaQTUMAgain);
			Assert.assertEquals(WalletFrozenQTUM, WalletFrozenQTUMAgain);
			Assert.assertEquals(Double.valueOf(df.format(WealthAllQTUM-redeemAmount-urgentFee)), WealthAllQTUMAgain);
			Assert.assertEquals(Double.valueOf(df.format(WealthMiningQTUM-redeemAmount-urgentFee)), WealthMiningQTUMAgain);
			Assert.assertEquals(WealthRedeemQTUM, WealthRedeemQTUMAgain);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
