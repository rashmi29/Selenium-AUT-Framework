package org.qa.appengine;

import java.io.IOException;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.qa.utils.*;

public class ApplicationSetup extends ExtentReportListener{
	public static Logger log = LogManager.getLogger(ApplicationSetup.class.getName());

	CommonUtilities objCU = new CommonUtilities();

	public static String testURL;
	public static String UID;
	public static String PAS;
	public static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
	String filepath = System.getProperty("user.dir") + "\\src\\main\\resources\\datapool\\" + "EnvData.properties";

	/**
	 * Launches browser and set URL,UID and PWD
	 * 
	 
	 * @param environment - environment name
	 * @param browser     - browser name
	 */
	@BeforeTest(alwaysRun = true)
	@Parameters({ "Environment", "Browser" })
	public synchronized void init(String environment, String browser) {

		if (environment.equalsIgnoreCase("QA")) {
			startBrowser(browser);
			System.out.println(filepath);
			setTestURL(objCU.readPropertyFile(filepath, "QAURL"));
		}
		if (environment.equalsIgnoreCase("Dev")) {
			startBrowser(browser);
			setTestURL(objCU.readPropertyFile(filepath, "DEVURL"));
		}
		
		setUID(objCU.readPropertyFile(filepath, "UID"));
		setPAS(objCU.readPropertyFile(filepath, "PWD"));
	}

	/**
	 * Start browser - instantiate driver & set implicit wait
	 * 
	 
	 * @param browser - browser name
	 * @return - driver instance
	 */
	public WebDriver startBrowser(String browser) {
		int wt = Integer.parseInt(objCU.readPropertyFile(filepath, "waitTimeout"));
		log.debug("Timeout time set to : = " + wt);

		if (browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.setPageLoadStrategy(PageLoadStrategy.NONE);

			driver.set(new ChromeDriver(options));
			getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(wt));
			log.debug(browser + " browser launch sucessfully");
		}

		else if (browser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver.set(new FirefoxDriver());
			getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(wt));
			log.debug(browser + " browser launch sucessfully");
		}

		log.debug("ThreadID in case of Parallel execution : " + Thread.currentThread().getId());
		return getDriver();
	}

	/**
	 * Close browser
	 * 
	 
	 */
	@AfterTest(alwaysRun = true)
	public synchronized void tearDown() {
		try {
			getDriver().quit();
			log.debug("Application Closed sucessfully");
			Runtime.getRuntime().exec("taskkill /F /IM chromedriver*");
		} catch (Exception e) {
			try {
				Runtime.getRuntime().exec("taskkill /F /IM chromedriver32.exe");
				log.debug("Application Closed force-fully ");
			} catch (IOException err) {
				log.error("IOException in <tearDown> " + err.getMessage());
			}
		}
	}

	/**
	 * This is used to get driver with thread local
	 * 
	 * @return
	 */
	public static synchronized WebDriver getDriver() {
		return driver.get();
	}

	/**
	 * returns test url
	 * 
	 * @return test url
	 */
	public String getTestURL() {
		return testURL;
	}

	/**
	 * Set test url
	 * 
	 * @param testURL - url to be set
	 */
	public void setTestURL(String testUrl) {
		testURL = testUrl;
	}

	/**
	 * returns user name/email/UID
	 * 
	 * @return user name
	 */
	public String getUID() {
		return UID;
	}

	/**
	 * Set user name/email/UID
	 * 
	 * @param uID - user name to be set
	 */
	public void setUID(String uID) {
		UID = uID;
	}

	/**
	 * returns PAS
	 * 
	 * @return PAS - password
	 */
	public String getPAS() {
		return PAS;
	}

	/**
	 * Set PAS - password
	 * 
	 * @param pAS - password
	 */
	public void setPAS(String pwd) {
		PAS = pwd;
	}

}
