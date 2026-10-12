package com.jdvg.automation.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Map;

/**
 * Factory for WebDriver instances. Selenium Manager (built into Selenium 4.6+)
 * downloads the matching driver binary, so no driver executables live in the repo.
 */
public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver create(Browser browser, boolean headless) {
        return switch (browser) {
            case CHROME -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--window-size=1366,900", "--disable-search-engine-choice-screen");
                // SauceDemo's public credentials trigger Chrome's "password found in a data breach"
                // dialog, which swallows clicks. Turn the password manager and leak check off.
                options.setExperimentalOption("prefs", Map.of(
                        "credentials_enable_service", false,
                        "profile.password_manager_enabled", false,
                        "profile.password_manager_leak_detection", false));
                if (headless) options.addArguments("--headless=new");
                yield new ChromeDriver(options);
            }
            case FIREFOX -> {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--width=1366", "--height=900");
                if (headless) options.addArguments("-headless");
                yield new FirefoxDriver(options);
            }
            case EDGE -> {
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--window-size=1366,900");
                if (headless) options.addArguments("--headless=new");
                yield new EdgeDriver(options);
            }
        };
    }
}
