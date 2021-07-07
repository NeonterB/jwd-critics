package com.epam.jwd_critics.pool;

import com.epam.jwd_critics.util.ApplicationPropertiesKeys;
import com.epam.jwd_critics.util.ApplicationPropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Abstract Factory pattern implementation. Provides with new connections.
 */
public class ConnectionFactory {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);

    public ConnectionFactory() {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Creates brand new db connection
     *
     * @return a connection to the database
     */
    Connection createConnection() {
        String url = ApplicationPropertiesLoader.get(ApplicationPropertiesKeys.DB_URL);
        String name = ApplicationPropertiesLoader.get(ApplicationPropertiesKeys.DB_NAME);
        String user = ApplicationPropertiesLoader.get(ApplicationPropertiesKeys.DB_USER);
        String password = ApplicationPropertiesLoader.get(ApplicationPropertiesKeys.DB_PASSWORD);
        try {
            return DriverManager.getConnection(url + "/" + name, user, password);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
