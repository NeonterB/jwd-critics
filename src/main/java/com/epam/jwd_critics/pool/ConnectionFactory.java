package com.epam.jwd_critics.pool;

import com.epam.jwd_critics.util.ApplicationProperties;
import com.epam.jwd_critics.util.ApplicationPropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);

    public ConnectionFactory() {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    Connection createConnection() {
        ApplicationProperties properties = ApplicationPropertiesLoader.getApplicationProperties();
        try {
            return DriverManager.getConnection(properties.getUrl() + "/" + properties.getDatabaseName(),
                    properties.getUser(), properties.getPassword());
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
