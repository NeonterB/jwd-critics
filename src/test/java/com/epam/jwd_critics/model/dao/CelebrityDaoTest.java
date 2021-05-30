package com.epam.jwd_critics.model.dao;

import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.model.entity.Celebrity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CelebrityDaoTest {
    private static final Logger logger = LoggerFactory.getLogger(CelebrityDaoTest.class);
    private static AbstractCelebrityDao celebrityDao;
    private static EntityTransaction transaction;
    private static Celebrity celebrity;

    @BeforeAll
    public static void initialize() {
        celebrityDao = CelebrityDao.getInstance();
        transaction = new EntityTransaction(celebrityDao);
        celebrity = Celebrity.newBuilder()
                .setFirstName("testFirstName")
                .setLastName("testLastName")
                .build();
    }

    @BeforeEach
    public void initializeTest() {
        try {
            celebrity = celebrityDao.create(celebrity);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testFindEntityById() {
        try {
            Celebrity actualResult = celebrityDao.getEntityById(celebrity.getId()).get();
            assertEquals(celebrity, actualResult);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testUpdate() {
        try {
            String oldFirstName = celebrity.getFirstName();
            celebrity.setFirstName("newTestFirstName");
            celebrityDao.update(celebrity);
            Celebrity actualResult = celebrityDao.getEntityById(celebrity.getId()).get();
            assertEquals(celebrity, actualResult);
            celebrity.setFirstName(oldFirstName);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testFindCrewByMovieId() {
        //System.out.println(celebrityDao.findCrewByMovieId(2));
    }

    @AfterEach
    public void endTest() {
        try {
            celebrityDao.delete(celebrity.getId());
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @AfterAll
    public static void clear() {
        transaction.rollback();
        transaction.close();
    }
}