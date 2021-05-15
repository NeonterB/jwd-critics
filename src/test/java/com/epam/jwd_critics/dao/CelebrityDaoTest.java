package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.exception.DaoException;
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
    public void initializeTest() throws DaoException {
        celebrity = celebrityDao.create(celebrity);
    }

    @Test
    public void testFindEntityById() throws DaoException {
        Celebrity actualResult = celebrityDao.findEntityById(celebrity.getId()).get();
        assertEquals(celebrity, actualResult);
    }

    @Test
    public void testUpdate() throws DaoException {
        String oldFirstName = celebrity.getFirstName();
        celebrity.setFirstName("newTestFirstName");
        celebrityDao.update(celebrity);
        Celebrity actualResult = celebrityDao.findEntityById(celebrity.getId()).get();
        assertEquals(celebrity, actualResult);
        celebrity.setFirstName(oldFirstName);
    }

    @Test
    public void testFindCrewByMovieId() throws DaoException{
        //System.out.println(celebrityDao.findCrewByMovieId(2));
    }

    @AfterEach
    public void endTest() throws DaoException {
        celebrityDao.deleteEntityById(celebrity.getId());
    }

    @AfterAll
    public static void clear() {
        transaction.rollback();
        transaction.close();
    }
}