package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.AgeRestriction;
import com.epam.jwd_critics.entity.Country;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.entity.Role;
import com.epam.jwd_critics.entity.Status;
import com.epam.jwd_critics.entity.User;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MovieReviewDaoTest {
    private static final Logger logger = LoggerFactory.getLogger(MovieReviewDaoTest.class);
    private static AbstractMovieReviewDao movieReviewDao;
    private static AbstractMovieDao movieDao;
    private static AbstractUserDao userDao;
    private static EntityTransaction transaction;
    private static MovieReview movieReview1;
    private static MovieReview movieReview2;
    private static MovieReview movieReview3;
    private static MovieReview movieReview4;
    private static Movie movie1;
    private static Movie movie2;
    private static User user1;
    private static User user2;

    @BeforeAll
    public static void initialize() {
        movieReviewDao = new MovieReviewDao();
        movieDao = new MovieDao();
        userDao = new UserDao();
        transaction = new EntityTransaction(movieReviewDao, movieDao, userDao);
        movie1 = Movie.newBuilder()
                .setName("Test Movie 1")
                .setSummary("Just a test movie")
                .setRuntime(Duration.parse("PT2H22M"))
                .setCountry(Country.USA)
                .setReleaseDate(LocalDate.parse("2021-09-20"))
                .setAgeRestriction(AgeRestriction.R)
                .build();
        movie2 = Movie.newBuilder()
                .setName("Test Movie 2")
                .setSummary("Just a test movie")
                .setRuntime(Duration.parse("PT2H22M"))
                .setCountry(Country.USA)
                .setReleaseDate(LocalDate.parse("2021-09-20"))
                .setAgeRestriction(AgeRestriction.R)
                .build();
        user1 = User.newBuilder()
                .setFirstName("Test")
                .setLastName("Testovich")
                .setEmail("test1@email.com")
                .setLogin("test1")
                .setPassword("test")
                .setStatus(Status.ACTIVE)
                .setRole(Role.ADMIN)
                .build();
        user2 = User.newBuilder()
                .setFirstName("Test")
                .setLastName("Testovich")
                .setEmail("test2@email.com")
                .setLogin("test2")
                .setPassword("test")
                .setStatus(Status.ACTIVE)
                .setRole(Role.ADMIN)
                .build();
        try {
            user1 = userDao.create(user1);
            user2 = userDao.create(user2);
            movie1 = movieDao.create(movie1);
            movie2 = movieDao.create(movie2);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
        movieReview1 = new MovieReview("test text", 80, user1.getId(), movie1.getId());
        movieReview2 = new MovieReview("test text", 70, user1.getId(), movie2.getId());
        movieReview3 = new MovieReview("test text", 60, user2.getId(), movie1.getId());
        movieReview4 = new MovieReview("test text", 50, user2.getId(), movie2.getId());
    }

    @AfterAll
    public static void clear() {
        transaction.rollback();
        transaction.close();
    }

    @BeforeEach
    public void initializeTest() {
        try {
            movieReview1 = movieReviewDao.create(movieReview1);
            movieReview2 = movieReviewDao.create(movieReview2);
            movieReview3 = movieReviewDao.create(movieReview3);
            movieReview4 = movieReviewDao.create(movieReview4);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testFindEntityById() {
        try {
            MovieReview actualResult = movieReviewDao.getEntityById(movieReview1.getId()).get();
            assertEquals(movieReview1, actualResult);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testUpdate() {
        try {
            String oldText = movieReview1.getText();
            movieReview1.setText("new test text");
            movieReviewDao.update(movieReview1);
            MovieReview actualResult = movieReviewDao.getEntityById(movieReview1.getId()).get();
            assertEquals(movieReview1, actualResult);
            movieReview1.setText(oldText);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testGetReviewsByMovieId() {
        try {
            int reviewCount = movieReviewDao.getCountByMovieId(movie1.getId());
            List<MovieReview> reviewsByMovieId = movieReviewDao.getMovieReviewsByMovieId(movie1.getId(), 0, reviewCount);
            assertTrue(reviewsByMovieId.contains(movieReview1));
            assertTrue(reviewsByMovieId.contains(movieReview3));
            assertEquals(reviewsByMovieId.size(), 2);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testGetReviewsByUserId() {
        try {
            int reviewCount = movieReviewDao.getCountByUserId(user1.getId());
            List<MovieReview> reviewsByMovieId = movieReviewDao.getMovieReviewsByUserId(user1.getId(), 0, reviewCount);
            assertTrue(reviewsByMovieId.contains(movieReview1));
            assertTrue(reviewsByMovieId.contains(movieReview2));
            assertEquals(reviewsByMovieId.size(), 2);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @AfterEach
    public void endTest() {
        try {
            movieReviewDao.delete(movieReview1.getId());
            movieReviewDao.delete(movieReview2.getId());
            movieReviewDao.delete(movieReview3.getId());
            movieReviewDao.delete(movieReview4.getId());
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }
}