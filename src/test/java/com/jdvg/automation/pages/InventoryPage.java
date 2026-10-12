package com.jdvg.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.math.BigDecimal;
import java.util.List;

public class InventoryPage extends BasePage {

    private static final By ITEM = dataTest("inventory-item");
    private static final By ITEM_NAME = dataTest("inventory-item-name");
    private static final By ITEM_PRICE = dataTest("inventory-item-price");
    private static final By SORT = dataTest("product-sort-container");
    private static final By CART_BADGE = dataTest("shopping-cart-badge");
    private static final By CART_LINK = dataTest("shopping-cart-link");
    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By LOGOUT = dataTest("logout-sidebar-link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return "Products".equals(title()) && !driver.findElements(ITEM).isEmpty();
    }

    public void addToCart(String productName) {
        item(productName).findElement(By.cssSelector("button[data-test^='add-to-cart']")).click();
    }

    public void sortBy(String visibleOption) {
        new Select(visible(SORT)).selectByVisibleText(visibleOption);
    }

    public List<String> productNames() {
        return texts(ITEM_NAME);
    }

    public List<BigDecimal> productPrices() {
        return texts(ITEM_PRICE).stream().map(Prices::parse).toList();
    }

    public int cartCount() {
        return isPresent(CART_BADGE) ? Integer.parseInt(text(CART_BADGE)) : 0;
    }

    /** Waits for the cart badge to reach the expected count (React updates it asynchronously). */
    public int waitForCartCount(int expected) {
        try {
            wait.until(d -> cartCount() == expected);
        } catch (TimeoutException ignored) {
            // fall through: the caller's assertion reports the actual count
        }
        return cartCount();
    }

    public CartPage openCart() {
        click(CART_LINK);
        return new CartPage(driver);
    }

    public LoginPage logout() {
        click(MENU_BUTTON);
        click(LOGOUT);
        return new LoginPage(driver);
    }

    private WebElement item(String productName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(ITEM));
        return driver.findElements(ITEM).stream()
                .filter(item -> item.findElement(ITEM_NAME).getText().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found on the inventory page: " + productName));
    }
}
