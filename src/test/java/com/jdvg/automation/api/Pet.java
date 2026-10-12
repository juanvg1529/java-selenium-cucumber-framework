package com.jdvg.automation.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Pet(long id, Category category, String name, List<String> photoUrls, List<Tag> tags, String status) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Category(long id, String name) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Tag(long id, String name) {
    }

    public Pet withName(String newName) {
        return new Pet(id, category, newName, photoUrls, tags, status);
    }

    public Pet withStatus(String newStatus) {
        return new Pet(id, category, name, photoUrls, tags, newStatus);
    }
}
