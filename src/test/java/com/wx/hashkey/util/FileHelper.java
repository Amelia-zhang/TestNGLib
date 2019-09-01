package com.wx.hashkey.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileHelper {
	static String fileName = ".\\tokens.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径

	public static String readFile() {
		/* 读入TXT文件 */

		File filename = new File(fileName); // 要读取以上路径的tokens.txt文件
		if (!filename.exists()) {
			return null;
		}

		InputStreamReader reader;
		try {
			reader = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
//		        String line = "";  
//		        line = br.readLine();  
			String line = br.readLine();
			String result = "";

			while (line != null) {
				result += line;
				line = br.readLine(); // 一次读入一行数据
			}
			return result;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 建立一个输入流对象reader
		return null;

	}

	public static String writeFile(String context) {
		/* 写入Txt文件 */
		File writename = new File(fileName); // 相对路径，如果没有则要建立一个新的output。txt文件
		try {
			if (!writename.exists()) {
				writename.createNewFile();
			}

			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			out.write(context); // \r\n即为换行
			out.flush(); // 把缓存区内容压入文件
			out.close(); // 最后记得关闭文件
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 创建新文件

		return null;

	}

}
