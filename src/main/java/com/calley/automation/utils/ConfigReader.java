package com.calley.automation.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Loads the config.properties file once and exposes typed getters
 * for all environment/browser/timeout/test-data settings.
 */
public class ConfigReader {

    private static final String CONFIG_PATH = "src/test/resources/config.properties";
    private static Properties properties;

    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            properties = new Properties();
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load config.properties from " + CONFIG_PATH, e);
        }
    }

    private ConfigReader() {
        // utility class
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in config.properties");
        }
        return value.trim();
    }

    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue).trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static String browser() {
        return get("browser", "chrome");
    }

    public static boolean headless() {
        return getBoolean("headless");
    }

    public static String registrationUrl() {
        return get("registration.url");
    }

    public static String loginUrl() {
        return get("login.url");
    }

    public static String baseUrl() {
        return get("base.url");
    }

    public static int implicitWait() {
        return getInt("implicit.wait");
    }

    public static int explicitWait() {
        return getInt("explicit.wait");
    }

    public static int pageLoadTimeout() {
        return getInt("page.load.timeout");
    }

    public static String planName() {
        return get("plan.name");
    }
}
