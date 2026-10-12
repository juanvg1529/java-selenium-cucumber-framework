package com.jdvg.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads config.properties, letting system properties (-Dkey=value) and
 * environment variables (KEY_NAME for key.name) override any value.
 */
public final class TestConfig {

    private static final Properties PROPERTIES = load();

    private TestConfig() {
    }

    public static String get(String key) {
        String fromSystem = System.getProperty(key);
        if (fromSystem != null && !fromSystem.isBlank()) return fromSystem;

        String fromEnv = System.getenv(key.toUpperCase().replace('.', '_'));
        if (fromEnv != null && !fromEnv.isBlank()) return fromEnv;

        String value = PROPERTIES.getProperty(key);
        if (value == null) throw new IllegalStateException("Missing configuration key: " + key);
        return value;
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) throw new IllegalStateException("config.properties not found on the classpath");
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read config.properties", e);
        }
        return properties;
    }
}
