package com.jdvg.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {

    private static final By HEADER = dataTest("complete-header");
    private static final By BACK_HOME = dataTest("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public String confirmation() {
        return text(HEADER);
    }

    public InventoryPage backHome() {
        click(BACK_HOME);
        return new InventoryPage(driver);
    }
}
