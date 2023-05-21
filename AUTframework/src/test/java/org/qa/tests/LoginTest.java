package org.qa.tests;

import org.openqa.selenium.WebDriver;
import org.qa.appengine.*;
import org.qa.utils.*;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import org.qa.businesslogic.*;

public class LoginTest extends ApplicationSetup{
	LoginBL objLoginBL = new LoginBL();
	CommonUtilities objCU = new CommonUtilities();

	@Test
	@Parameters({"Browser"})
	public void LoginTC(String browser) {
		WebDriver driver = ApplicationSetup.getDriver();
		test.set(extent.createTest("Login TC" + " - " + browser));
		objCU.browserTCcounter(browser);

		objLoginBL.launchApplication(driver, ApplicationSetup.testURL);
		objLoginBL.userLogin(driver, ApplicationSetup.UID, ApplicationSetup.PAS);			
	}

}
