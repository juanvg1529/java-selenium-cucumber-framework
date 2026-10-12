package com.jdvg.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CartPage extends BasePage {

    private static final By ITEM = dataTest("inventory-item");
    private static final By ITEM_NAME = dataTest("inventory-item-name");
    private static final By CHECKOUT = dataTest("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public List<String> itemNames() {
        visible(CHECKOUT);
        return texts(ITEM_NAME);
    }

    public void remove(String productName) {
        visible(CHECKOUT); // the cart page has finished rendering
        int before = driver.findElements(ITEM).size();
        // Lookup and click are retried together: the list can re-render between finding a row and clicking it.
        wait.ignoring(StaleElementReferenceException.class).until(d -> {
            d.findElements(ITEM).stream()
                    .filter(item -> item.findElement(ITEM_NAME).getText().equals(productName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Product not in the cart: " + productName))
                    .findElement(By.cssSelector("button[data-test^='remove']"))
                    .click();
            return true;
        });
        wait.until(ExpectedConditions.numberOfElementsToBe(ITEM, before - 1));
    }

    public CheckoutInformationPage checkout() {
        click(CHECKOUT);
        return new CheckoutInformationPage(driver);
    }
}
