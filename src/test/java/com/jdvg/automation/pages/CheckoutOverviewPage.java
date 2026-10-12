package com.jdvg.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;
import java.util.List;

public class CheckoutOverviewPage extends BasePage {

    private static final By ITEM_NAME = dataTest("inventory-item-name");
    private static final By ITEM_PRICE = dataTest("inventory-item-price");
    private static final By PAYMENT = dataTest("payment-info-value");
    private static final By SHIPPING = dataTest("shipping-info-value");
    private static final By SUBTOTAL = dataTest("subtotal-label");
    private static final By TAX = dataTest("tax-label");
    private static final By TOTAL = dataTest("total-label");
    private static final By FINISH = dataTest("finish");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public List<String> itemNames() {
        visible(FINISH);
        return texts(ITEM_NAME);
    }

    public List<BigDecimal> itemPrices() {
        return texts(ITEM_PRICE).stream().map(Prices::parse).toList();
    }

    public String paymentInfo() {
        return text(PAYMENT);
    }

    public String shippingInfo() {
        return text(SHIPPING);
    }

    public BigDecimal subtotal() {
        return Prices.parse(text(SUBTOTAL));
    }

    public BigDecimal tax() {
        return Prices.parse(text(TAX));
    }

    public BigDecimal total() {
        return Prices.parse(text(TOTAL));
    }

    public CheckoutCompletePage finish() {
        click(FINISH);
        return new CheckoutCompletePage(driver);
    }
}
