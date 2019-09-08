package com.wx.hashkey.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExcelDataXls {
	public Workbook workbook;
	public Sheet sheet;
	public Cell cell;
	int rows;
	int columns;
	public String fileName;
	public String caseName;
	public ArrayList<String> arrkey = new ArrayList<String>();
	String sourceFile;

	/**
	 * @param fileName excel文件名
	 * @param caseName sheet名
	 */
	public ExcelDataXls(String fileName, String caseName) {
		super();
		this.fileName = fileName;
		this.caseName = caseName;
	}

	/**
	 * 获得excel表中的数据
	 */
	public Object[][] getExcelData() throws BiffException, IOException {
		InputStream instream = new FileInputStream(getPath());
		Workbook workbook = Workbook.getWorkbook(instream);
		
		//workbook = Workbook.getWorkbook(new File(getPath()));
		sheet = workbook.getSheet(caseName);
		rows = sheet.getRows();
		columns = sheet.getColumns();
		// 为了返回值是Object[][],定义一个多行单列的二维数组
		HashMap<String, String>[][] arrmap = new HashMap[rows - 1][1];
		// 对数组中所有元素hashmap进行初始化
		if (rows > 1) {
			for (int i = 0; i < rows - 1; i++) {
				arrmap[i][0] = new HashMap<String, String>();
			}
		} else {
			System.out.println("excel中没有数据");
		}

		// 获得首行的列名，作为hashmap的key值
		for (int c = 0; c < columns; c++) {
			String cellvalue = sheet.getCell(c, 0).getContents();
			arrkey.add(cellvalue);
		}
		// 遍历所有的单元格的值添加到hashmap中
		for (int r = 1; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				String cellvalue = sheet.getCell(c, r).getContents();
				arrmap[r - 1][0].put(arrkey.get(c), cellvalue);
			}
		}
//		for (HashMap<String, String>[] hashMaps : arrmap) {
//			//System.out.println(hashMaps);
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
                + fileName + ".xls";
        //System.out.println(sourceFile);
        return sourceFile;
    }

}
