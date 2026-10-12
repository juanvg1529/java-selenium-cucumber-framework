package com.jdvg.automation.api;

import com.jdvg.automation.config.TestConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/** One method per Petstore endpoint. Returns raw responses; assertions belong to the steps. */
public class PetStoreClient {

    private final RequestSpecification spec = new RequestSpecBuilder()
            .setBaseUri(TestConfig.get("api.baseUrl"))
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .log(LogDetail.URI)
            .build();

    public Response create(Pet pet) {
        return given(spec).body(pet).post("/pet");
    }

    public Response get(long id) {
        return given(spec).get("/pet/{id}", id);
    }

    public Response update(Pet pet) {
        return given(spec).body(pet).put("/pet");
    }

    public Response delete(long id) {
        return given(spec).delete("/pet/{id}", id);
    }

    public Response findByStatus(String status) {
        return given(spec).queryParam("status", status).get("/pet/findByStatus");
    }
}
