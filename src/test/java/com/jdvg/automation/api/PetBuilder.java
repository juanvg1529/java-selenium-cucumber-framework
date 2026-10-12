package com.jdvg.automation.api;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class PetBuilder {

    private PetBuilder() {
    }

    /**
     * A pet with a random id. The old suite used a fixed id (1529), which collides with
     * anyone else using the shared public Petstore and with parallel runs.
     */
    public static Pet aPet(String name, String tag) {
        long id = ThreadLocalRandom.current().nextLong(9_000_000_000L, 9_999_999_999L);
        return new Pet(id, new Pet.Category(1, "Dogs"), name, List.of("https://example.com/" + id + ".jpg"),
                List.of(new Pet.Tag(1, tag)), "available");
    }
}
