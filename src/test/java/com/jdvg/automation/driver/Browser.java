package com.jdvg.automation.driver;

public enum Browser {
    CHROME, FIREFOX, EDGE;

    public static Browser from(String name) {
        try {
            return valueOf(name.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported browser '" + name + "'. Use chrome, firefox or edge.", e);
        }
    }
}
