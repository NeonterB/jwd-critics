package com.epam.jwd_critics.pool;

import com.epam.jwd_critics.entity.ApplicationProperties;
import com.epam.jwd_critics.exception.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);

    private BlockingQueue<ConnectionProxy> availableConnections;
    private Queue<ConnectionProxy> unavailableConnections;

    private ConnectionFactory factory = new ConnectionFactory();

    private AtomicInteger poolSize = new AtomicInteger();
    private final Lock lock = new ReentrantLock();

    private ConnectionPool() {
        availableConnections = new LinkedBlockingQueue<>(ApplicationProperties.getInstance().getMaxPoolSize());
        unavailableConnections = new ArrayDeque<>(ApplicationProperties.getInstance().getMaxPoolSize());
        initPool();
    }

    private static class ConnectionPoolSingleton {
        private static final ConnectionPool INSTANCE = new ConnectionPool();
    }

    public static ConnectionPool getInstance() {
        return ConnectionPoolSingleton.INSTANCE;
    }

    private void initPool() {
        ApplicationProperties properties = ApplicationProperties.getInstance();
        for (int i = 0; i < properties.getMinPoolSize(); i++) {
            try (ConnectionProxy connection = new ConnectionProxy(factory.createConnection())) {
                availableConnections.add(connection);
                poolSize.incrementAndGet();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        logger.info("Connection pool initialized");
    }

    public Connection getConnection() {
        lock.lock();
        ConnectionProxy connection = null;
        ApplicationProperties properties = ApplicationProperties.getInstance();
        try {
            if (!availableConnections.isEmpty()) {
                connection = availableConnections.take();
            } else if (poolSize.get() < properties.getMaxPoolSize()) {
                connection = new ConnectionProxy(factory.createConnection());
            } else {
                //todo wait
            }
            unavailableConnections.offer(connection);
            logger.debug("Connection taken");
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            lock.unlock();
        }
        return connection;
    }

    public void returnConnection(Connection connection) throws ConnectionException {
        lock.lock();
        try {
            if (connection instanceof ConnectionProxy) {
                if (unavailableConnections.contains(connection)) {
                    availableConnections.offer((ConnectionProxy) connection);
                    unavailableConnections.remove(connection);
                }
                logger.info("Connection returned");
            } else {
                logger.error("Failed to return connection");
                throw new ConnectionException("Not an instance of ConnectionProxy");
            }
        } finally {
            lock.unlock();
        }
    }

    void removeUnnecessaryConnections() {
        //todo
    }

    void destroyPool() {
        //todo
    }
}
