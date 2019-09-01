package com.wx.hashkey.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

public class ExcelReader {
	public static final String SAMPLE_XLSX_FILE_PATH = "D:\\zhangyu\\java\\TestHashkeyHub2\\src\\test\\resources\\testdata.xlsx";
	public static ArrayList<String> arrKey = new ArrayList<String>();

	public static Object[][] excelReader() throws IOException, InvalidFormatException {

		// Creating a Workbook from an Excel file (.xls or .xlsx)
		Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));

		// Getting the Sheet at index zero
		Sheet sheet = workbook.getSheetAt(0);
		// 获得总列数
		int coloumNum = sheet.getRow(1).getPhysicalNumberOfCells();
		// 获得总行数
		int rowNum = sheet.getLastRowNum() + 1;
		// 为了返回值是Object[][],定义一个多行单列的二维数组
		HashMap<String, String>[][] arrmap = new HashMap[rowNum - 1][1];
		if (rowNum > 1) {
			for (int i = 0; i < rowNum - 1; i++) {
				arrmap[i][0] = new HashMap<String, String>();
			}
		} else {
			System.out.println("excel中没有数据");
		}

		DataFormatter dataFormatter = new DataFormatter();
		// 获得首行的列名，作为hashmap的key值
		for (int c = 0; c < coloumNum; c++) {
			Cell cell = sheet.getRow(0).getCell(c);
			String cellValue = dataFormatter.formatCellValue(cell);
			arrKey.add(cellValue);
		}
		// 遍历所有的单元格的值添加到hashmap中
		for (int r = 1; r < rowNum; r++) {
			for (int c = 0; c < coloumNum; c++) {
				Cell cell = sheet.getRow(r).getCell(c);
				String cellValue = dataFormatter.formatCellValue(cell);
				arrmap[r - 1][0].put(arrKey.get(c), cellValue);
			}
		}

//		for (HashMap<String, String>[] hashMaps : arrmap) {
//			// System.out.println(hashMaps);
//			for (HashMap<String, String> hashMaps2 : hashMaps) {
//				System.out.println(hashMaps2);
//			}
//		}
		return arrmap;

		// return arrmap;

	}

}
