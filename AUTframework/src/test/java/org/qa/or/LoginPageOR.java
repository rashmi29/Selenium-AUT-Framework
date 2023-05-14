package org.qa.or;

import org.openqa.selenium.By;
import org.openqa.selenium.support.locators.RelativeLocator;

public class LoginPageOR {
	
	public By unameTxt = By.name("username");
	public By pwdTxt = By.name("password");
	public By LoginBtn = RelativeLocator.with(By.tagName("button")).below(By.name("password"));

}
