package com.jdvg.automation.steps;

import com.jdvg.automation.context.TestContext;
import com.jdvg.automation.pages.CartPage;
import com.jdvg.automation.pages.CheckoutCompletePage;
import com.jdvg.automation.pages.CheckoutInformationPage;
import com.jdvg.automation.pages.CheckoutOverviewPage;
import com.jdvg.automation.pages.InventoryPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ShoppingSteps {

    private final TestContext context;

    public ShoppingSteps(TestContext context) {
        this.context = context;
    }

    private InventoryPage inventory() {
        return new InventoryPage(context.driver());
    }

    private CheckoutOverviewPage overview() {
        return new CheckoutOverviewPage(context.driver());
    }

    @When("the customer adds these products to the cart:")
    public void theCustomerAddsTheseProductsToTheCart(List<String> products) {
        InventoryPage inventory = inventory();
        for (String product : products) {
            inventory.addToCart(product);
            context.cartItems.add(product);
        }
        assertThat(inventory.waitForCartCount(context.cartItems.size())).as("cart badge").isEqualTo(context.cartItems.size());
    }

    @When("the customer sorts the products by {string}")
    public void theCustomerSortsTheProductsBy(String option) {
        inventory().sortBy(option);
    }

    @When("the customer adds the product in position {int} to the cart")
    public void theCustomerAddsTheProductInPosition(int position) {
        String product = inventory().productNames().get(position - 1);
        theCustomerAddsTheseProductsToTheCart(List.of(product));
    }

    @When("the customer removes {string} from the cart")
    public void theCustomerRemovesFromTheCart(String product) {
        CartPage cart = inventory().openCart();
        cart.remove(product);
        context.cartItems.remove(product);
        assertThat(cart.itemNames()).containsExactlyElementsOf(context.cartItems);
    }

    @Then("the cart badge shows {int}")
    public void theCartBadgeShows(int count) {
        assertThat(inventory().waitForCartCount(count)).isEqualTo(count);
    }

    @When("the customer checks out as {string} {string} with postal code {string}")
    public void theCustomerChecksOutAs(String firstName, String lastName, String postalCode) {
        CartPage cart = new CartPage(context.driver());
        if (!context.driver().getCurrentUrl().contains("cart")) cart = inventory().openCart();
        assertThat(cart.itemNames()).as("cart contents").containsExactlyElementsOf(context.cartItems);
        cart.checkout().continueWith(firstName, lastName, postalCode);
    }

    @Then("the checkout error {string} is shown")
    public void theCheckoutErrorIsShown(String error) {
        assertThat(new CheckoutInformationPage(context.driver()).error()).isEqualTo(error);
    }

    @Then("the order overview lists exactly the products in the cart")
    public void theOrderOverviewListsExactlyTheProductsInTheCart() {
        assertThat(overview().itemNames()).containsExactlyElementsOf(context.cartItems);
    }

    @Then("the order is paid with {string} and shipped by {string}")
    public void theOrderIsPaidWithAndShippedBy(String payment, String shipping) {
        CheckoutOverviewPage overview = overview();
        assertThat(overview.paymentInfo()).contains(payment);
        assertThat(overview.shippingInfo()).contains(shipping);
    }

    @Then("the order total is the item total plus tax")
    public void theOrderTotalIsTheItemTotalPlusTax() {
        CheckoutOverviewPage overview = overview();
        BigDecimal sumOfItems = overview.itemPrices().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        assertThat(overview.subtotal()).as("item total = sum of item prices").isEqualByComparingTo(sumOfItems);
        assertThat(overview.total()).as("total = item total + tax").isEqualByComparingTo(overview.subtotal().add(overview.tax()));
    }

    @When("the customer finishes the order")
    public void theCustomerFinishesTheOrder() {
        overview().finish();
    }

    @Then("the order is confirmed with {string}")
    public void theOrderIsConfirmedWith(String message) {
        assertThat(new CheckoutCompletePage(context.driver()).confirmation()).isEqualTo(message);
    }

    @Then("the cart is empty")
    public void theCartIsEmpty() {
        InventoryPage inventory = new CheckoutCompletePage(context.driver()).backHome();
        assertThat(inventory.cartCount()).isZero();
    }

    @Then("the products are ordered by {word} {word}")
    public void theProductsAreOrderedBy(String field, String direction) {
        boolean descending = direction.equals("descending");
        if (field.equals("name")) {
            Comparator<String> order = descending ? Comparator.reverseOrder() : Comparator.naturalOrder();
            assertThat(inventory().productNames()).isSortedAccordingTo(order);
        } else {
            Comparator<BigDecimal> order = descending ? Comparator.reverseOrder() : Comparator.naturalOrder();
            assertThat(inventory().productPrices()).isSortedAccordingTo(order);
        }
    }
}
