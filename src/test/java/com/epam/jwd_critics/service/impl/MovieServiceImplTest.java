package com.epam.jwd_critics.service.impl;

import com.epam.jwd_critics.entity.AgeRestriction;
import com.epam.jwd_critics.entity.Country;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.exception.MovieServiceException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.exception.codes.MovieServiceCode;
import com.epam.jwd_critics.service.MovieService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieServiceImplTest {
    private static final Logger logger = LoggerFactory.getLogger(MovieServiceImplTest.class);
    private static final MovieService movieService = MovieServiceImpl.getInstance();
    private static Movie movie = Movie.newBuilder()
            .setName("Test Movie")
            .setSummary("Just a test movie")
            .setRuntime(Duration.parse("PT2H22M"))
            .setCountry(Country.USA)
            .setReviewCount(0)
            .setRating(0)
            .setReviews(Collections.emptyList())
            .setGenres(Collections.emptyList())
            .setStaff(Collections.emptyMap())
            .setReleaseDate(LocalDate.parse("2021-09-20"))
            .setAgeRestriction(AgeRestriction.R)
            .build();

    @BeforeAll
    public static void initializeTest() {
        try {
            movie = movieService.create(movie);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testFindEntityById() {
        try {
            Movie actualResult = movieService.findById(movie.getId()).get();
            assertEquals(movie, actualResult);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testUpdate() {
        try {
            String oldSummary = movie.getSummary();
            movie.setSummary("new summary");
            movieService.update(movie);
            Movie actualResult = movieService.findById(movie.getId())
                    .orElseThrow(() -> new MovieServiceException(MovieServiceCode.MOVIE_DOES_NOT_EXIST));
            assertEquals(movie, actualResult);
            movie.setSummary(oldSummary);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @AfterEach
    public void endTest() {
        try {
            movieService.delete(movie.getId());
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }
    }
}