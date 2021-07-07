package com.epam.jwd_critics.pool;

import com.epam.jwd_critics.exception.ConnectionException;
import com.epam.jwd_critics.util.ApplicationPropertiesKeys;
import com.epam.jwd_critics.util.ApplicationPropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Threadsafe pool of connections to database
 */
public class ConnectionPool {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);

    private final BlockingQueue<ConnectionProxy> availableConnections;
    private final Queue<ConnectionProxy> unavailableConnections;

    private final ConnectionFactory factory = new ConnectionFactory();

    private final int minPoolSize;
    private final int maxPoolSize;

    private final AtomicInteger poolSize = new AtomicInteger();
    private final Lock lock = new ReentrantLock();
    private final Condition lockCondition = lock.newCondition();

    private ConnectionPool() {
        minPoolSize = Integer.parseInt(ApplicationPropertiesLoader.get(ApplicationPropertiesKeys.DB_MAX_POOL_SIZE));
        maxPoolSize = Integer.parseInt(ApplicationPropertiesLoader.get(ApplicationPropertiesKeys.DB_MIN_POOL_SIZE));
        availableConnections = new LinkedBlockingQueue<>(minPoolSize);
        unavailableConnections = new ArrayDeque<>(maxPoolSize);
        initPool();
    }

    public static ConnectionPool getInstance() {
        return ConnectionPoolSingleton.INSTANCE;
    }

    /**
     * Initiates pool. Populates availableConnections with new connections
     */
    private void initPool() {
        for (int i = 0; i < maxPoolSize; i++) {
            ConnectionProxy connection = new ConnectionProxy(factory.createConnection());
            availableConnections.add(connection);
            poolSize.incrementAndGet();
        }
        logger.debug("Connection pool initialized");
    }

    /**
     * Looks for available connections, gets one, and puts it in unavailableConnections collection. If there aren't
     * any and max pool size is yet to be reached, creates new connection. Otherwise, waits for available connections.
     *
     * @return available connection
     */
    public Connection getConnection() {
        lock.lock();
        ConnectionProxy connection = null;
        try {
            if (!availableConnections.isEmpty()) {
                connection = availableConnections.take();
            } else if (poolSize.get() < minPoolSize) {
                connection = new ConnectionProxy(factory.createConnection());
            } else {
                while (availableConnections.isEmpty() && poolSize.get() < minPoolSize) {
                    lockCondition.await();
                }
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

    /**
     * Returns connection to availableConnections, signals that there is an available connection.
     *
     * @param connection to be returned
     * @throws ConnectionException if unavailableConnections does not contain given connection
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    public void returnConnection(Connection connection) throws ConnectionException {
        lock.lock();
        try {
            if (unavailableConnections.contains(connection)) {
                availableConnections.offer((ConnectionProxy) connection);
                unavailableConnections.remove(connection);
                lockCondition.signalAll();
                logger.debug("Connection returned");
            } else {
                throw new ConnectionException();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Closes all connections.
     *
     * @throws ConnectionException if error occurred while closing connection
     */
    void destroyPool() throws ConnectionException {
        for (int i = 0; i < poolSize.get(); i++) {
            try {
                availableConnections.take().hardClose();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                throw new ConnectionException(e);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
        logger.debug("Connection pool destroyed");
    }

    private static class ConnectionPoolSingleton {
        private static final ConnectionPool INSTANCE = new ConnectionPool();
    }
}
