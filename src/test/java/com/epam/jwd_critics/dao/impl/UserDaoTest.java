package com.epam.jwd_critics.dao.impl;

import com.epam.jwd_critics.dao.UserDao;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.util.PropertiesLoaderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class UserDaoTest {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoTest.class);
    UserDao userDao = new UserDaoImpl();

    @BeforeAll
    static void loadProperties() {
        PropertiesLoaderUtil.loadProperties();
    }

    @Test
    void findAll() {
        try {
            Assertions.assertEquals(userDao.findAll().size(), 2);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    void get() {
        try {
            Assertions.assertEquals(userDao.get(1), 2);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    void delete() {
        try{
            int sizeBefore = userDao.findAll().size();
            userDao.delete(3);
            int sizeAfter = userDao.findAll().size();
            Assertions.assertEquals(sizeAfter, sizeBefore - 1);
            sizeBefore = userDao.findAll().size();
            userDao.delete("test");
            sizeAfter = userDao.findAll().size();
            Assertions.assertEquals(sizeAfter, sizeBefore - 1);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    void create() {
        try{
            int sizeBefore = userDao.findAll().size();
            User user = User.newBuilder()
                    .setFirstName("Test")
                    .setLastName("Testovich")
                    .setEmail("test@email.com")
                    .setLogin("test")
                    .setPassword("test")
                    .setRating(0)
                    .setStatus(1)
                    .setRole(1)
                    .build();
            User updatedUser = userDao.create(user);
            int sizeAfter = userDao.findAll().size();
            Assertions.assertEquals(sizeAfter - 1, sizeBefore);
            Assertions.assertEquals(updatedUser.getId(), 4);
            user.setRating(-10);
            userDao.delete(user.getId());
            Assertions.assertThrows(Exception.class, () -> userDao.create(user));
            userDao.delete(user.getId());
            user.setRating(10);
            user.setLogin("neonter");
            Assertions.assertThrows(Exception.class, () -> userDao.create(user));
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    void update() {

    }
}