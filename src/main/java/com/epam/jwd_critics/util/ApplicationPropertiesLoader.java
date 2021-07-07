package com.epam.jwd_critics.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class ApplicationPropertiesLoader {

    private final Logger logger = LoggerFactory.getLogger(ApplicationPropertiesLoader.class);

    private final Properties properties = new Properties();

    private final Map<String, String> appProperties = new HashMap<>();

    private ApplicationPropertiesLoader() {
        loadProperties();
    }

    public static String get(String key) {
        String value = PropertiesLoaderUtilSingleton.INSTANCE.appProperties.get(key);
        if (value == null) {
            try (InputStream input = ApplicationPropertiesLoader.class.getClassLoader().getResourceAsStream("properties/application.properties")) {
                PropertiesLoaderUtilSingleton.INSTANCE.properties.load(input);
                value = PropertiesLoaderUtilSingleton.INSTANCE.properties.getProperty(key);
            } catch (IOException e) {
                PropertiesLoaderUtilSingleton.INSTANCE.logger.error(e.getMessage(), e);
            }
        }
        return value;
    }

    private void loadProperties() {
        try (InputStream input = ApplicationPropertiesLoader.class.getClassLoader().getResourceAsStream("properties/application.properties")) {
            properties.load(input);
            for (String key : properties.stringPropertyNames()) {
                String value = properties.getProperty(key);
                appProperties.put(key, value);
            }
            logger.debug("Application properties loaded");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static class PropertiesLoaderUtilSingleton {
        private static final ApplicationPropertiesLoader INSTANCE = new ApplicationPropertiesLoader();
    }
}
