package com.jdvg.automation.steps;

import com.jdvg.automation.api.Pet;
import com.jdvg.automation.api.PetBuilder;
import com.jdvg.automation.api.PetStoreClient;
import com.jdvg.automation.context.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class PetStoreSteps {

    private final TestContext context;
    private final PetStoreClient petStore = new PetStoreClient();

    public PetStoreSteps(TestContext context) {
        this.context = context;
    }

    @Given("a new pet named {string} tagged {string}")
    public void aNewPetNamedTagged(String name, String tag) {
        context.pet = PetBuilder.aPet(name, tag);
    }

    @When("the pet is added to the store")
    public void thePetIsAddedToTheStore() {
        context.lastResponse = petStore.create(context.pet);
    }

    @When("the pet's name is changed to {string} and its status to {string}")
    public void thePetsNameIsChangedAndItsStatus(String name, String status) {
        context.pet = context.pet.withName(name).withStatus(status);
        context.lastResponse = petStore.update(context.pet);
    }

    @When("the pet is deleted")
    public void thePetIsDeleted() {
        context.lastResponse = petStore.delete(context.pet.id());
    }

    @When("the customer lists pets with status {string}")
    public void theCustomerListsPetsWithStatus(String status) {
        context.lastResponse = petStore.findByStatus(status);
    }

    @When("the customer requests the pet with id {long}")
    public void theCustomerRequestsThePetWithId(long id) {
        context.lastResponse = petStore.get(id);
    }

    @Then("the response status is {int}")
    public void theResponseStatusIs(int status) {
        assertThat(context.lastResponse.statusCode()).as(context.lastResponse.asPrettyString()).isEqualTo(status);
    }

    @Then("the store returns the pet exactly as sent")
    public void theStoreReturnsThePetExactlyAsSent() {
        assertThat(context.lastResponse.as(Pet.class)).as("response body").isEqualTo(context.pet);

        // The public Petstore runs behind several nodes that sync with a small delay, so a read
        // right after a write can briefly return the old version. Poll instead of failing.
        Response stored = eventually(() -> petStore.get(context.pet.id()),
                response -> response.statusCode() == 200 && response.as(Pet.class).equals(context.pet));
        assertThat(stored.as(Pet.class)).as("stored pet").isEqualTo(context.pet);
    }

    @Then("the pet can no longer be found")
    public void thePetCanNoLongerBeFound() {
        Response response = eventually(() -> petStore.get(context.pet.id()), r -> r.statusCode() == 404);
        assertThat(response.statusCode()).isEqualTo(404);
        context.pet = null; // nothing left to clean up
    }

    @Then("every pet in the list has status {string}")
    public void everyPetInTheListHasStatus(String status) {
        List<String> statuses = context.lastResponse.jsonPath().getList("status", String.class);
        assertThat(statuses).isNotEmpty().allMatch(status::equals);
    }

    @Then("the error message is {string}")
    public void theErrorMessageIs(String message) {
        assertThat(context.lastResponse.jsonPath().getString("message")).isEqualTo(message);
    }

    private static Response eventually(Supplier<Response> request, Predicate<Response> done) {
        Response response = request.get();
        long deadline = System.nanoTime() + Duration.ofSeconds(10).toNanos();
        while (!done.test(response) && System.nanoTime() < deadline) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            response = request.get();
        }
        return response;
    }
}
