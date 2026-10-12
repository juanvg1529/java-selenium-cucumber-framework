package com.jdvg.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME = dataTest("username");
    private static final By PASSWORD = dataTest("password");
    private static final By LOGIN_BUTTON = dataTest("login-button");
    private static final By ERROR = dataTest("error");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open(String baseUrl) {
        driver.get(baseUrl);
        visible(LOGIN_BUTTON);
        return this;
    }

    public void loginAs(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(LOGIN_BUTTON);
    }

    public boolean isDisplayed() {
        try {
            return visible(LOGIN_BUTTON).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String error() {
        return text(ERROR);
    }
}
