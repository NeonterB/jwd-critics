package com.epam.jwd_critics.model.pool;

import com.epam.jwd_critics.exception.ConnectionException;
import com.epam.jwd_critics.model.util.ApplicationProperties;
import com.epam.jwd_critics.model.util.PropertiesLoaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);

    private final BlockingQueue<ConnectionProxy> availableConnections;
    private final Queue<ConnectionProxy> unavailableConnections;

    private final ConnectionFactory factory = new ConnectionFactory();

    private final AtomicInteger poolSize = new AtomicInteger();
    private final Lock lock = new ReentrantLock();
    private final Condition lockCondition = lock.newCondition();

    private ConnectionPool() {
        availableConnections = new LinkedBlockingQueue<>(PropertiesLoaderUtil.getApplicationProperties().getMaxPoolSize());
        unavailableConnections = new ArrayDeque<>(PropertiesLoaderUtil.getApplicationProperties().getMaxPoolSize());
        initPool();
    }

    public static ConnectionPool getInstance() {
        return ConnectionPoolSingleton.INSTANCE;
    }

    private static class ConnectionPoolSingleton {
        private static final ConnectionPool INSTANCE = new ConnectionPool();
    }

    private void initPool() {
        ApplicationProperties properties = PropertiesLoaderUtil.getApplicationProperties();
        for (int i = 0; i < properties.getMinPoolSize(); i++) {
            ConnectionProxy connection = new ConnectionProxy(factory.createConnection());
            availableConnections.add(connection);
            poolSize.incrementAndGet();
        }
        logger.info("Connection pool initialized");
    }

    public Connection getConnection() {
        lock.lock();
        ConnectionProxy connection = null;
        ApplicationProperties properties = PropertiesLoaderUtil.getApplicationProperties();
        try {
            if (!availableConnections.isEmpty()) {
                connection = availableConnections.take();
            } else if (poolSize.get() < properties.getMaxPoolSize()) {
                connection = new ConnectionProxy(factory.createConnection());
            } else {
                while (availableConnections.isEmpty() && poolSize.get() < properties.getMaxPoolSize()) {
                    lockCondition.await();
                }
            }
            unavailableConnections.offer(connection);
            logger.info("Connection taken");
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            lock.unlock();
        }
        return connection;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public void returnConnection(Connection connection) throws ConnectionException {
        lock.lock();
        try {
            if (unavailableConnections.contains(connection)) {
                availableConnections.offer((ConnectionProxy) connection);
                unavailableConnections.remove(connection);
                lockCondition.signalAll();
                logger.info("Connection returned");
            } else {
                throw new ConnectionException();
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
