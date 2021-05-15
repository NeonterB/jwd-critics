package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.AgeRestriction;
import com.epam.jwd_critics.entity.Country;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.exception.DaoException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieDaoTest {
    private static final Logger logger = LoggerFactory.getLogger(MovieDaoTest.class);
    private static AbstractMovieDao movieDao;
    private static EntityTransaction transaction;
    private static Movie movie;

    @BeforeAll
    public static void initialize() {
        movieDao = MovieDao.getInstance();
        transaction = new EntityTransaction(movieDao);
        movie = Movie.newBuilder()
                .setName("Test Movie")
                .setSummary("Just a test movie")
                .setRuntime(Duration.parse("PT2H22M"))
                .setCountry(Country.USA)
                .setReleaseDate(LocalDate.parse("2021-09-20"))
                .setAgeRestriction(AgeRestriction.R)
                .build();
    }

    @BeforeEach
    public void initializeTest() throws DaoException {
        movie = movieDao.create(movie);
    }

    @Test
    public void testFindEntityById() throws DaoException {
        Movie actualResult = movieDao.findEntityById(movie.getId()).get();
        assertEquals(movie, actualResult);
    }

    @Test
    public void testUpdate() throws DaoException {
        String oldSummary = movie.getSummary();
        movie.setSummary("new summary");
        movieDao.update(movie);
        Movie actualResult = movieDao.findEntityById(movie.getId()).get();
        assertEquals(movie, actualResult);
        movie.setSummary(oldSummary);
    }

    @Test
    public void testFindMoviesByCelebrityId() throws DaoException {
        //System.out.println(movieDao.findMoviesByCelebrityId(6));
    }

    @AfterEach
    public void endTest() throws DaoException {
        movieDao.deleteEntityById(movie.getId());
    }

    @AfterAll
    public static void clear() {
        transaction.rollback();
        transaction.close();
    }
}