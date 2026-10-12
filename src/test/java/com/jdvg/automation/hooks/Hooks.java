package com.jdvg.automation.hooks;

import com.jdvg.automation.api.PetStoreClient;
import com.jdvg.automation.context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    /** Attaches a screenshot of the failing page to the report, then always closes the browser. */
    @After(order = 0)
    public void closeBrowser(Scenario scenario) {
        if (!context.hasDriver()) return;
        try {
            if (scenario.isFailed()) {
                byte[] screenshot = ((TakesScreenshot) context.driver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Page at failure");
            }
        } finally {
            context.quitDriver();
        }
    }

    /** Leaves the shared public Petstore clean, even when the scenario failed half-way. */
    @After(value = "@api", order = 1)
    public void deletePet() {
        if (context.pet != null) new PetStoreClient().delete(context.pet.id());
    }
}
