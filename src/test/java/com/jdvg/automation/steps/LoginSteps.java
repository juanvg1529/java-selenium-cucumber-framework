package com.jdvg.automation.steps;

import com.jdvg.automation.config.TestConfig;
import com.jdvg.automation.context.TestContext;
import com.jdvg.automation.pages.InventoryPage;
import com.jdvg.automation.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginSteps {

    private final TestContext context;

    public LoginSteps(TestContext context) {
        this.context = context;
    }

    private LoginPage loginPage() {
        return new LoginPage(context.driver());
    }

    @Given("the customer is on the login page")
    public void theCustomerIsOnTheLoginPage() {
        loginPage().open(TestConfig.get("ui.baseUrl"));
    }

    @Given("the customer is logged in")
    public void theCustomerIsLoggedIn() {
        theCustomerIsOnTheLoginPage();
        theCustomerLogsInWithValidCredentials();
        theProductsPageIsDisplayed();
    }

    @When("the customer logs in with valid credentials")
    public void theCustomerLogsInWithValidCredentials() {
        loginPage().loginAs(TestConfig.get("ui.username"), TestConfig.get("ui.password"));
    }

    @When("the customer logs in as {string} with password {string}")
    public void theCustomerLogsInAs(String username, String password) {
        loginPage().loginAs(username, password);
    }

    @Then("the products page is displayed")
    public void theProductsPageIsDisplayed() {
        assertThat(new InventoryPage(context.driver()).isDisplayed()).as("products page displayed").isTrue();
    }

    @When("the customer logs out")
    public void theCustomerLogsOut() {
        new InventoryPage(context.driver()).logout();
    }

    @Then("the login page is displayed")
    public void theLoginPageIsDisplayed() {
        assertThat(loginPage().isDisplayed()).as("login page displayed").isTrue();
    }

    @Then("the login error {string} is shown")
    public void theLoginErrorIsShown(String error) {
        assertThat(loginPage().error()).isEqualTo(error);
    }
}
