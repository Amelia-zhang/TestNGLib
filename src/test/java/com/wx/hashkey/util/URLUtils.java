package com.wx.hashkey.util;

import java.net.MalformedURLException;
import java.net.URL;

public class URLUtils {

//	public static void main(String[] args) {
//		String webUrl = "https://qa-api.hub.hashkey.com/passport/kyc";
//		URL url;
//		try {
//			url = new URL(webUrl);
//			String path=url.getFile();
//			System.out.println(path);
//			String[] split = path.split("\\/");
//			String secretUrl="";
//			String finalUrl="";
//			for (int i = 2; i < split.length; i++) {
//				secretUrl="/"+split[i];
//				finalUrl+=secretUrl;
//			}
//			System.out.println("finalUrl为"+finalUrl);
//			System.out.println(path.substring(path.indexOf("/",1)));
//			
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//
//	}
	
	public static String getUrl(String webUrl) {
		String secretURL="";
		try {
			URL url=new URL(webUrl);
			String path=url.getFile();
			secretURL = path.substring(path.indexOf("/",1));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return secretURL;
		
	}

}
