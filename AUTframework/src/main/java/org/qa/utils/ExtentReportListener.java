package org.qa.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.qa.appengine.*;
import net.lingala.zip4j.exception.ZipException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExtentReportListener implements ITestListener {
	public static Logger log = LogManager.getLogger(ExtentReportListener.class.getName());
	RWExcel objExcel = new RWExcel();
	CommonUtilities objCU = new CommonUtilities();

	public static ExtentSparkReporter htmlReporter;
	public static ExtentReports extent;
	public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	public static int TestStepCount = 0;
	public static String Environment, groups;
	public static int chromeTC = 0;
	public static int firefoxTC = 0;
	String ssFlag = objExcel.readCell("Configuration", "SCREENSHOT_FLAG");

	/**
	 * Initialize report and set required values for report
	 * 
	 * @param environment - environment name
	 * @param browser     - browser name
	 * @param groups      - group name
	 */
	@BeforeSuite(alwaysRun = true)
	@Parameters({ "Environment", "groups" })
	public synchronized void initReport(String environment, String groups) {
		Date now = new Date();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		String monthName = new SimpleDateFormat("MMMM").format(now);
		int monthday = Calendar.getInstance().get(Calendar.DATE);

		ExtentReportListener.Environment = environment;
		ExtentReportListener.groups = groups;

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMMyyyy-HHmm");
		String date = simpleDateFormat.format(new Date());

		String file = System.getProperty("user.dir") + "\\src\\main\\resources\\datapool\\" + "EnvData.properties";
		String reportFolderPath = objCU.readPropertyFile(file, "TestReport");
		String reportFileName = objExcel.readCell("Configuration", "REPORT_FILENAME");
		String documentTitle = objExcel.readCell("Configuration", "DOCUMENT_TITLE");
		String reportName = objExcel.readCell("Configuration", "REPORT_NAME");
		String appName = objExcel.readCell("Configuration", "APPLICATION_NAME");

		// Create an object of Extent Reports
		log.debug("Initilizating Test Report.... ");
		htmlReporter = new ExtentSparkReporter(
				System.getProperty("user.dir") + reportFolderPath + "\\TestReports\\" + year + "\\" + monthName + "\\"
						+ monthday + "\\" + reportFileName + Environment + groups + "_" + date + ".html");

		log.debug("Test Path: " + System.getProperty("user.dir") + reportFolderPath + "\\TestReports" + year + "\\"
				+ monthName + "\\" + monthday + "\\" + reportFileName + Environment + groups + "_" + date + ".html");

		htmlReporter.config().setDocumentTitle(documentTitle); //
		htmlReporter.config().setReportName("<B>" + reportName + "</B>");
		htmlReporter.config().setTheme(Theme.STANDARD);

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Application :", appName);
		extent.setSystemInfo("Environment :", environment);
		extent.setSystemInfo("Suite :", groups);
	}

	public static ThreadLocal<ExtentTest> getTest() {
		return test;
	}

	public static void setTest(ThreadLocal<ExtentTest> test) {
		ExtentReportListener.test = test;
	}

	/**
	 * Capture and copy Screen shot for Test Steps as per the test step result
	 * 
	 * @param result   - result (pass/ failed/ Warning/ exception)
	 * @param tcStep   - test case step
	 * @param tcActual - actual test case step
	 * @param mthName  - test method name
	 */
	public synchronized void stepsResult(String result, String tcStep, String tcActual, String mthName) {
		TestStepCount++;
		try {
			String ssName = tcStep.substring(0, 15);

			// Setting up TEST STEP
			test.get().log(Status.INFO, MarkupHelper
					.createLabel(TestStepCount + ": <B>Test STEP : </B> " + tcStep + " ", ExtentColor.BLUE));

			if (result.equalsIgnoreCase("pass")) {

				test.get().log(Status.PASS, MarkupHelper.createLabel("<B>Result : </B>\" " + tcActual + " \" is PASSED",
						ExtentColor.GREEN));

				if (ssFlag.equalsIgnoreCase("on")) {
					String screenshotPath = objCU.CaptureScreenShot(ApplicationSetup.getDriver(), ssName + mthName,
							result);
					test.get().log(Status.PASS, MarkupHelper
							.createLabel("Screent shot attached for PASSED STEP > " + mthName, ExtentColor.GREEN));
					test.get().pass("ScreenShot for PASS Step " + test.get().addScreenCaptureFromPath(screenshotPath));
				}
			} else if (result.equalsIgnoreCase("failed")) {

				test.get().log(Status.FAIL,
						MarkupHelper.createLabel("<B>Result : </B>\" " + tcActual + " \" is FAILED", ExtentColor.RED));

				String screenshotPath = objCU.CaptureScreenShot(ApplicationSetup.getDriver(), ssName + mthName, result);
				test.get().log(Status.FAIL, MarkupHelper
						.createLabel("Screent shot attached for Failed STEP > " + mthName, ExtentColor.RED));
				test.get().fail("ScreenShot for FAILED Step " + test.get().addScreenCaptureFromPath(screenshotPath));
			} else if (result.equalsIgnoreCase("Warning") || result.equalsIgnoreCase("exception")) {

				test.get().log(Status.WARNING, MarkupHelper
						.createLabel("<B>Result : </B>\" " + tcActual + " \" has warning", ExtentColor.RED));

				String screenshotPath = objCU.CaptureScreenShot(ApplicationSetup.getDriver(), ssName + mthName, result);
				test.get().log(Status.WARNING,
						MarkupHelper.createLabel("Screent shot attached for the STEP > " + mthName, ExtentColor.RED));
				test.get().fail(
						"ScreenShot for Step trigger Warning " + test.get().addScreenCaptureFromPath(screenshotPath));
			}

			extent.flush();
		} catch (Exception e) {
			test.get().log(Status.INFO, "Failed to captuer SS ");
			log.error("Error in <stepsResult> " + e.getMessage());
			extent.flush();
		}
	}

	public synchronized void onTestStart(ITestResult result) {
		log.info("-------------------------------------------------------------------------------");
		log.info("Test started: " + result.getName());
	}

	public synchronized void onTestSuccess(ITestResult result) {

	}

	public synchronized void onTestFailure(ITestResult result) {

	}

	public synchronized void onTestSkipped(ITestResult result) {
	}

	public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	public synchronized void onTestFailedWithTimeout(ITestResult result) {

	}

	public synchronized void onStart(ITestContext context) {
		objCU.folderCleanUp();

	}

	public synchronized void onFinish(ITestContext context) {

	}

	@AfterSuite(alwaysRun = true)
	public void repostStats() throws IOException {
		log.debug("Generating final Test Report.....");
		if (ExtentReportListener.chromeTC != 0) {
			extent.setSystemInfo("Number of TC executed on Chrome :", String.valueOf(ExtentReportListener.chromeTC));
		}
		if (ExtentReportListener.firefoxTC != 0) {
			extent.setSystemInfo("Number of TC executed on Firefox :", String.valueOf(ExtentReportListener.firefoxTC));
		}

		extent.flush();
		try {
			objCU.zipper(Environment, groups);
		} catch (ZipException e) {
			log.error("Error in <onFinish> " + e.getMessage());
		}
	}

}
