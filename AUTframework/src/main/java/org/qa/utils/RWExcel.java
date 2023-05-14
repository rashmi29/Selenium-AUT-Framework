package org.qa.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RWExcel {
	public static Logger log = LogManager.getLogger(RWExcel.class.getName());

	XSSFWorkbook workbook;
	XSSFSheet sheet;
	XSSFRow row;
	XSSFCell cell;
	String tdMod;
	CommonUtilities objCU = new CommonUtilities();
	String filepath = System.getProperty("user.dir") + "\\src\\main\\resources\\datapool\\" + "EnvData.properties";
	String resourceFolderPath = objCU.readPropertyFile(filepath, "ResourcesPath");
	String filePath = System.getProperty("user.dir") + resourceFolderPath + ("\\dataPool\\TestData.xlsx");

	int rc, cc;

	/**
	 * Construct XSSFWorkbook object to read excel file
	 * 
	 * @param sheetName - work sheet name
	 */
	public void readExcel(String sheetName) {
		File file;
		FileInputStream fin;
		try {
			file = new File(filePath);
			fin = new FileInputStream(file);
			workbook = new XSSFWorkbook(fin);
			fin.close();
		} catch (FileNotFoundException e) {
			log.error("Excel file not found: " + filePath);
		} catch (IOException e) {
			log.error("Error while reading a Test Data file <readExcel>: " + e.getMessage());
		}
	}

	/**
	 * Count the number for rows in sheet
	 * 
	 * @param sheetName - work sheet name
	 * @return - count of used rows
	 */
	public int countRows(String sheetName) {
		readExcel(sheetName);
		sheet = workbook.getSheet(sheetName);
		return sheet.getLastRowNum() - sheet.getFirstRowNum();
	}

	/**
	 * Count the number for columns in sheet
	 * 
	 * @param sheetName - work sheet name
	 * @return - count of used columns
	 */
	public int countCol(String sheetName) {
		sheetName = sheetName.trim();
		readExcel(sheetName);
		row = workbook.getSheet(sheetName).getRow(0);

		return cc = row.getLastCellNum() - row.getFirstCellNum();
	}

	/**
	 * read specific column value from row 2 in excel
	 * 
	 * @param sheetName - work sheet name
	 * @param colName   - name of column name
	 * @return - test data for given column name
	 */
	public String readCell(String sheetName, String colName) {
		sheetName = sheetName.trim();
		String cellValue = null;
		int cc = countCol(sheetName);
		for (int i = 0; i < cc; i++) {
			String fetchValue = row.getCell(i).getStringCellValue().trim();

			if (colName.equalsIgnoreCase(fetchValue)) {
				row = workbook.getSheet(sheetName).getRow(1);
				XSSFCell cell = row.getCell(i);
				if (!(cell == null)) {
					if (cell.getCellType() == CellType.STRING) {
						cellValue = row.getCell(i).getStringCellValue();
					} else if (cell.getCellType() == CellType.BOOLEAN) {
						cellValue = String.valueOf(row.getCell(i).getBooleanCellValue());
					} else if (cell.getCellType() == CellType.NUMERIC) {
						cellValue = String.valueOf(row.getCell(i).getNumericCellValue());
					} else if (cell.getCellType() == CellType.FORMULA) {
						try {
							cellValue = row.getCell(i).getStringCellValue();
						} catch (Exception e) {
							cellValue = NumberToTextConverter.toText(row.getCell(i).getNumericCellValue());
						}
					}

				} else {
					cellValue = "";
				}
				break;
			}
		}
		return cellValue;
	}

	public int getTestDataCount(String sheetName) {
		File file;
		FileInputStream fin;
		sheetName = sheetName.trim();
		int totalTestDataCount = 1;
		try {
			file = new File(filePath);
			fin = new FileInputStream(file);
			workbook = new XSSFWorkbook(fin);

			sheet = workbook.getSheet(sheetName);
			totalTestDataCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
			fin.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found ");
			totalTestDataCount = 1;
		} catch (IOException e) {
			System.out.println("Error while reading a Test Data file ");
			totalTestDataCount = 1;
		}
		return totalTestDataCount;
	}
}
