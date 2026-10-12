package com.jdvg.automation.context;

import com.jdvg.automation.api.Pet;
import com.jdvg.automation.config.TestConfig;
import com.jdvg.automation.driver.Browser;
import com.jdvg.automation.driver.DriverFactory;
import io.restassured.response.Response;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * Per-scenario state, created by PicoContainer and injected into hooks and step classes.
 * Replaces the old static driver and ScenarioContext: nothing is shared between scenarios,
 * which is what makes parallel execution safe.
 */
public class TestContext {

    private WebDriver driver;

    /** Products the scenario put in the cart, in the order they were added. */
    public final List<String> cartItems = new ArrayList<>();

    public Pet pet;
    public Response lastResponse;

    public WebDriver driver() {
        if (driver == null) {
            driver = DriverFactory.create(Browser.from(TestConfig.get("browser")), TestConfig.getBoolean("headless"));
        }
        return driver;
    }

    public boolean hasDriver() {
        return driver != null;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
