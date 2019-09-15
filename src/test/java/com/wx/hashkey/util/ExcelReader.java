package com.wx.hashkey.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;


public class ExcelReader {
	//public static final String SAMPLE_XLSX_FILE_PATH = ".\\src\\test\\resources\\testdata.xlsx";
	public ArrayList<String> arrKey = new ArrayList<String>();
	public String fileName;
	public String caseName;
	static String sourceFile;
	public Sheet sheet;
	public Cell cell;
	int rows;
	int columns;
	
	public ExcelReader() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param fileName excel文件名
	 * @param caseName sheet名
	 */
	public ExcelReader(String fileName, String caseName) {
		super();
		this.fileName = fileName;
		this.caseName = caseName;
	}



	public Object[][] excelReader() throws IOException, InvalidFormatException {
		// Creating a Workbook from an Excel file (.xls or .xlsx)
		Workbook workbook = WorkbookFactory.create(new File(getPath()));
		sheet = workbook.getSheet(caseName);
		// 获得总列数
		int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells();
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

		
	}
	
	/**
     * 获得excel文件的路径
     * @return
     * @throws IOException
     */
    public String getPath() throws IOException {
        File directory = new File(".");
        sourceFile = directory.getCanonicalPath() + "\\src\\test\\resources\\"
                + fileName + ".xlsx";
        //System.out.println(sourceFile);
        return sourceFile;
    }

}
