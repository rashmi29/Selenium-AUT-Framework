package org.qa.businesslogic;

import org.qa.utils.*;
import org.testng.Assert;
import org.openqa.selenium.WebDriver;
import org.qa.or.*;

public class LoginBL extends ExtentReportListener {
	CommonMethods objCM = new CommonMethods();
	LoginPageOR objLoginOR = new LoginPageOR();
	NavigationOR objNavOR = new NavigationOR();
	String mthName;

	public void launchApplication(WebDriver driver, String url) {
		try {
			mthName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			if (objCM.launchURL(driver, url)) {
				Assert.assertTrue(true);
				stepsResult("PASS", "Launching url in browser.", "Application launched successfully", mthName);
				Thread.sleep(5000);
			} else {
				Assert.assertTrue(false);
				stepsResult("FAILED", "Launching url in browser.", "Application is NOT launched successfully", mthName);
			}
		} catch (Exception e) {
			stepsResult("EXCEPTION", "Launching url in browser.", "Exception while launching url.", mthName);
		}

	}

	public void userLogin(WebDriver driver, String username, String password) {
		try {
			mthName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			Thread.sleep(5000);
			objCM.WDWait(driver, objLoginOR.LoginBtn);

			objCM.WDWait(driver, objLoginOR.unameTxt);

			objCM.setText(driver, objLoginOR.unameTxt, username);
			objCM.setText(driver, objLoginOR.pwdTxt, password);
			objCM.click(driver, objLoginOR.LoginBtn);
			Thread.sleep(4000);

			if (objCM.isElementPresent(driver, objNavOR.sidemenu)) {
				stepsResult("PASS", "User is entering valid credentials and clicking on login",
						"Used logged in to application successfully", mthName);
				Assert.assertTrue(true);
			} else {
				stepsResult("FAILED", "User is entering valid credentials and clicking on login",
						"Used is NOT logged in to application successfully", mthName);
				driver.quit();
				Assert.assertTrue(false, "<Login failed>");
			}
		} catch (Exception e) {
			stepsResult("Exception", "User is entering valid credentials and clicking on login",
					"Used is NOT logged in to application successfully", mthName);
			Assert.assertTrue(false);
			driver.quit();
		}

	}
}
