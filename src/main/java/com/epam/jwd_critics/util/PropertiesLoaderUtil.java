package com.epam.jwd_critics.util;


import com.epam.jwd_critics.entity.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesLoaderUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesLoaderUtil.class);

    private static final Properties properties = new Properties();

    private PropertiesLoaderUtil() {
    }

    public static void loadProperties() {
        ApplicationProperties appProperties = ApplicationProperties.getInstance();

        try (InputStream input = PropertiesLoaderUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(input);
            appProperties.setUrl(properties.getProperty("url"));
            appProperties.setDatabaseName(properties.getProperty("databaseName"));
            appProperties.setUser(properties.getProperty("user"));
            appProperties.setPassword(properties.getProperty("password"));
            appProperties.setMaxPoolSize(Integer.parseInt(properties.getProperty("maxPoolSize")));
            appProperties.setMinPoolSize(Integer.parseInt(properties.getProperty("minPoolSize")));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        logger.debug("Application properties loaded");
    }
}
