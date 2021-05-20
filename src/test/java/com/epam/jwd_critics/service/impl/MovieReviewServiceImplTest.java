package com.epam.jwd_critics.service.impl;

import com.epam.jwd_critics.entity.AgeRestriction;
import com.epam.jwd_critics.entity.Country;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.service.MovieReviewService;
import com.epam.jwd_critics.service.MovieService;
import com.epam.jwd_critics.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MovieReviewServiceImplTest {
    private static final Logger logger = LoggerFactory.getLogger(MovieReviewServiceImplTest.class);
    private static final MovieService movieService = MovieServiceImpl.getInstance();
    private static final UserService userService = UserServiceImpl.getInstance();
    private static final MovieReviewService reviewService = MovieReviewServiceImpl.getInstance();
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
                .setEmail("test@email.com")
                .setLogin("test1")
                .setPassword("test")
                .setRating(0)
                .setStatus(1)
                .setRole(1)
                .build();
        user2 = User.newBuilder()
                .setFirstName("Test")
                .setLastName("Testovich")
                .setEmail("test@email.com")
                .setLogin("test2")
                .setPassword("test")
                .setRating(0)
                .setStatus(1)
                .setRole(1)
                .build();
        try {
            user1 = userService.register(user1.getFirstName(),
                    user1.getLastName(),
                    user1.getEmail(),
                    user1.getLogin(),
                    user1.getPassword().toCharArray());
            user2 = userService.register(user2.getFirstName(),
                    user2.getLastName(),
                    user2.getEmail(),
                    user2.getLogin(),
                    user2.getPassword().toCharArray());

            movie1 = movieService.create(movie1);
            movie2 = movieService.create(movie2);

            movieReview1 = new MovieReview("test text", 80, user1.getId(), movie1.getId());
            movieReview2 = new MovieReview("test text", 70, user1.getId(), movie2.getId());
            movieReview3 = new MovieReview("test text", 60, user2.getId(), movie1.getId());
            movieReview4 = new MovieReview("test text", 50, user2.getId(), movie2.getId());

            movieReview1 = reviewService.create(movieReview1);
            movieReview2 = reviewService.create(movieReview2);
            movieReview3 = reviewService.create(movieReview3);
            movieReview4 = reviewService.create(movieReview4);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);

        }
    }

    @Test
    public void testFindEntityById() {
        try {
            MovieReview actualResult = reviewService.getEntityById(movieReview1.getId()).get();
            assertEquals(movieReview1, actualResult);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testUpdate() {
        try {
            String oldText = movieReview1.getText();
            movieReview1.setText("new test text");
            reviewService.update(movieReview1);
            MovieReview actualResult = reviewService.getEntityById(movieReview1.getId()).get();
            assertEquals(movieReview1, actualResult);
            movieReview1.setText(oldText);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testGetReviewsByMovieId() {
        try {
            List<MovieReview> reviewsByMovieId = movieService.getEntityById(movie1.getId()).get().getReviews();
            assertTrue(reviewsByMovieId.contains(movieReview1));
            assertTrue(reviewsByMovieId.contains(movieReview3));
            assertEquals(reviewsByMovieId.size(), 2);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testGetReviewsByUserId() {
        try {
            List<MovieReview> reviewsByMovieId = userService.getEntityById(user1.getId()).get().getReviews();
            assertTrue(reviewsByMovieId.contains(movieReview1));
            assertTrue(reviewsByMovieId.contains(movieReview2));
            assertEquals(reviewsByMovieId.size(), 2);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @AfterAll
    public static void endTest() {
        try {
            userService.delete(user1.getId());
            userService.delete(user2.getId());
            movieService.delete(movie1.getId());
            movieService.delete(movie2.getId());
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }
    }
}