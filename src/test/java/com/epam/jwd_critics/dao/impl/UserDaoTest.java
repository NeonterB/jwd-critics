package com.epam.jwd_critics.dao.impl;

import com.epam.jwd_critics.dao.BaseDao;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.DaoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class UserDaoTest {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoTest.class);
    BaseDao userDao = new UserDaoImpl();

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
            System.out.println(userDao.getEntityById(1).get());
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    void delete() {
        try {
            int sizeBefore = userDao.findAll().size();
            userDao.deleteEntityById(10);
            int sizeAfter = userDao.findAll().size();
            Assertions.assertEquals(sizeAfter, sizeBefore - 1);
            sizeBefore = userDao.findAll().size();
            userDao.deleteEntityById("test");
            sizeAfter = userDao.findAll().size();
            Assertions.assertEquals(sizeAfter, sizeBefore - 1);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    void create() {
        try {
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
            userDao.deleteEntityById(user.getId());
            Assertions.assertThrows(Exception.class, () -> userDao.create(user));
            userDao.deleteEntityById(user.getId());
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