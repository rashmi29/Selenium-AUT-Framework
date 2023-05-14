package org.qa.utils;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonMethods {
	public static Logger log = LogManager.getLogger(CommonMethods.class.getName());
	CommonUtilities objCU = new CommonUtilities();

	Boolean flag = null;
	WebDriverWait wait;
	String parenwindow = "", Newcurrentwin = "";

	/**
	 * Launch URL in browser and maximize browser
	 * 
	 * @param driver - WebDriver instance
	 * @param url    - URL to open
	 * @return true if URL is launched
	 */
	public boolean launchURL(WebDriver driver, String url) {
		try {
			driver.get(url);
			driver.manage().window().maximize();
			log.debug("Launching url: " + url);
			return true;
		} catch (Exception e) {
			log.error("Error in <launchURL> " + e.getMessage());
			return false;
		}
	}

	/**
	 * Identify the object on UI and highlight the object boundary with RED border
	 * 
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @return - WebElement
	 */
	public WebElement objecLocater(WebDriver driver, By locator) {
		WDWait(driver, locator);
		WebElement objTemp = driver.findElement(locator);
		if (objTemp.isDisplayed() || objTemp.isEnabled()) {

			try {
				objCU.objHig(driver, objTemp);
				log.debug("Object is Visible/Enabled: " + locator);
				return objTemp;
			} catch (Exception e) {
				log.error("Object failed to be highlighted: " + locator);
				return null;
			}

		} else {
			log.error("Object has not built:  " + locator);
			throw new ElementNotInteractableException(null, null);
		}

	}

	/**
	 * Waits until visibility of element OR max 30 sec
	 * 
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @return WebElement - value if the function returned something different from
	 *         null or false before the timeout expired.
	 */
	public WebElement WDWait(WebDriver driver, By locator) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	/**
	 * Identify the all objects on UI and highlight the objects boundary with RED
	 * border
	 * 
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @return - list of web elements matching with locator
	 */
	public List<WebElement> listObjecLocater(WebDriver driver, By locator) {
		List<WebElement> objTemp = driver.findElements(locator);
		for (WebElement obj2 : objTemp) {
			if (obj2.isDisplayed()) {
				objCU.objHig(driver, obj2);
			}
		}
		return objTemp;
	}

	/**
	 * Clears and sets text in text box and highlight the text box boundary with RED
	 * border
	 * 
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @param txtVar  - text to set in elements
	 * @return boolean
	 */
	public boolean setText(WebDriver driver, By locator, String txtVar) {
		boolean flag;
		try {
			objecLocater(driver, locator);
			WebElement wd = driver.findElement(locator);
			wd.clear();
			wd.click();
			wd.sendKeys(txtVar);
			flag = true;
			log.debug("Setting text <" + txtVar + "> in field with locator: " + locator);
		} catch (Exception e) {
			flag = false;
			log.error("Error in <setText> " + e.getMessage());
		}

		return flag;
	}

	/**
	 * Get text from element
	 * 
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @return text from element
	 */
	public String getText(WebDriver driver, By locator) {
		try {
			log.debug("Getting text from element with locator: " + locator);
			return objecLocater(driver, locator).getText();
		} catch (Exception e) {
			log.error("Error in <getText> " + e.getMessage());
			return driver.findElement(locator).getText();
		}
	}

	/**
	 * Clicking on element identified by given locator
	 * 
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @return boolean
	 */
	public boolean click(WebDriver driver, By locator) {
		WebElement objWE = objecLocater(driver, locator);

		if (objWE.isEnabled()) {
			log.debug("Clicking on element with locator: " + locator);
			objWE.click();
			return true;
		} else
			return false;
	}

	/**
	 * Checks if element present or not
	 * 
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @return boolean
	 */
	public boolean isElementPresent(WebDriver driver, By locator) {
		try {
			objecLocater(driver, locator);
			return true;
		} catch (org.openqa.selenium.NoSuchElementException e) {
			log.error("Error in <isElementPresent> " + e.getMessage());
			return false;
		}
	}

}