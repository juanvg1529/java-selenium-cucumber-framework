package com.jdvg.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutInformationPage extends BasePage {

    private static final By FIRST_NAME = dataTest("firstName");
    private static final By LAST_NAME = dataTest("lastName");
    private static final By POSTAL_CODE = dataTest("postalCode");
    private static final By CONTINUE = dataTest("continue");
    private static final By ERROR = dataTest("error");

    public CheckoutInformationPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutOverviewPage continueWith(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME, firstName);
        type(LAST_NAME, lastName);
        type(POSTAL_CODE, postalCode);
        click(CONTINUE);
        return new CheckoutOverviewPage(driver);
    }

    public String error() {
        return text(ERROR);
    }
}
