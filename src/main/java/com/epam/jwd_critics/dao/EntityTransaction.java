package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.BaseEntity;
import com.epam.jwd_critics.pool.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class EntityTransaction {
    private Connection connection;
    private static final Logger logger = LoggerFactory.getLogger(EntityTransaction.class.getName());

    public void init(AbstractBaseDao<?, ? extends BaseEntity>... daos) {
        try {
            if (connection == null) {
                connection = ConnectionPool.getInstance().getConnection();
            }
            connection.setAutoCommit(false);
            for (AbstractBaseDao<?, ? extends BaseEntity> dao : daos) {
                dao.setConnection(connection);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void end() {
        try {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        connection = null;
    }
}
