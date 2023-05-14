package org.qa.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.io.FileHandler;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class CommonUtilities {
	public static Logger log = LogManager.getLogger(CommonUtilities.class.getName());
	WebDriver driver;
	Properties prop;

	String filepath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator
			+ "resources" + File.separator + "datapool" + File.separator + "EnvData.properties";
	File file = new File(filepath);
	String resourcesPath = this.readPropertyFile(filepath, "ResourcesPath");
	String reportsFolderPath = this.readPropertyFile(filepath, "TestReport");

	/**
	 * Highlight element on UI
	 * 
	 * @param driver  - driver instance
	 * @param element - element to be highlighted
	 */
	public void objHig(WebDriver driver, WebElement element) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].style.border='3px solid red'", element);
	}

	/**
	 * Reading values from property file
	 * 
	 * @param key - Key which need to be read from properties file
	 * @return - value of that specific key
	 */
	public String readPropertyFile(String filePath, String key) {
		String value = null;
		try {
			prop = new Properties();
			FileInputStream fin = new FileInputStream(filePath);
			prop.load(fin);
			value = prop.getProperty(key);
		} catch (Exception e) {
			log.error("Error while reading property file");
			log.error(e.getMessage());
			return null;
		}
		return value;
	}

	/**
	 * Capture screenshot for full screen
	 * 
	 * @param driver   - driver instance
	 * @param tcName   - name of test case
	 * @param tcStatus - status of test case (Pass / Failed)
	 * @return - screenshot location
	 */
	public String CaptureScreenShot(WebDriver driver, String tcName, String tcStatus) {

		String tcSSPath = System.getProperty("user.dir") + reportsFolderPath;
		String passTCfolder = "\\Pass\\";
		String failTCfolder = "\\Failed\\";
		String destinationFolder = null;
		String fileName = new SimpleDateFormat("yyyyMMMDDhhmmss").format(new Date());

		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		try {
			if (tcStatus.equalsIgnoreCase("Pass")) {
				destinationFolder = tcSSPath + passTCfolder + tcName + "_" + fileName + "_Pass.png";
				FileHandler.copy(srcFile, new File(destinationFolder));

			} else if (tcStatus.equalsIgnoreCase("Failed")) {
				destinationFolder = tcSSPath + failTCfolder + tcName + "_" + fileName + "Failed.png";
				FileHandler.copy(srcFile, new File(destinationFolder));
			}
		} catch (IOException e) {
			log.error("Error in zipping report " + e.getMessage());
		}
		return destinationFolder;
	}

	/**
	 * Clean the files from Reports folder as per flag
	 */
	public void folderCleanUp() {
		try {
			RWExcel objExcel = new RWExcel();
			String reportflag = objExcel.readCell("Configuration", "PRESERVE_REPORT");
			if (reportflag.equalsIgnoreCase("false")) {
				File r[] = new File[4];
				r[0] = new File(System.getProperty("user.dir") + reportsFolderPath + "\\TestReports\\");
				r[1] = new File(System.getProperty("user.dir") + reportsFolderPath + "\\Failed\\");
				r[2] = new File(System.getProperty("user.dir") + reportsFolderPath + "\\Pass\\");
				r[3] = new File(System.getProperty("user.dir") + reportsFolderPath + "\\zip\\");

				for (File i : r) {
					File[] filelist = i.listFiles();
					for (File fl : filelist) {
						if (!fl.getName().contains(".txt")) {
							if (fl.isFile())
								fl.delete();
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Error in zipping report " + e.getMessage());
		}
	}

	/**
	 * zip report folder and save to zip folder under resources
	 * 
	 * @param env - environment name
	 * @param grp - group name
	 * @throws IOException
	 */
	public void zipper(String env, String grp) throws IOException {
		ZipFile zp = null;
		try {
			DateFormat df = new SimpleDateFormat("ddMMMYYY-HHmm");
			Date dt = new Date();

			zp = new ZipFile(System.getProperty("user.dir") + resourcesPath + "\\zip\\Report_" + env + "_" + grp + "_"
					+ df.format(dt) + ".zip");

			zp.addFolder(new File(System.getProperty("user.dir") + resourcesPath + "\\Reports\\"));

		} catch (ZipException e) {
			log.error("Error in zipping report " + e.getMessage());
		} finally {
			zp.addFolder(new File(System.getProperty("user.dir") + resourcesPath + "\\Reports\\"));
			zp.close();
		}
	}

	public synchronized void browserTCcounter(String browserName) {
		if (browserName.equalsIgnoreCase("chrome")) {
			ExtentReportListener.chromeTC = ExtentReportListener.chromeTC + 1;
		} else if (browserName.equalsIgnoreCase("firefox")) {
			ExtentReportListener.firefoxTC = ExtentReportListener.firefoxTC + 1;
		}
	}
}
