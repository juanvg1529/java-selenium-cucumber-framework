package com.jdvg.automation.pages;

import com.jdvg.automation.config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/** Explicit-wait helpers shared by every page. No implicit waits, no Thread.sleep. */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.getInt("timeout.seconds")));
    }

    /** SauceDemo marks every element we need with a stable data-test attribute. */
    protected static By dataTest(String value) {
        return By.cssSelector("[data-test='" + value + "']");
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String text) {
        WebElement field = visible(locator);
        field.clear();
        field.sendKeys(text);
    }

    protected String text(By locator) {
        return visible(locator).getText();
    }

    /** Texts of all matching elements, re-read if React re-renders the list while reading it. */
    protected List<String> texts(By locator) {
        return wait.ignoring(StaleElementReferenceException.class)
                .until(d -> d.findElements(locator).stream().map(WebElement::getText).toList());
    }

    protected boolean isPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    /** The page heading SauceDemo shows on every logged-in screen ("Products", "Your Cart", ...). */
    public String title() {
        return text(dataTest("title"));
    }
}
