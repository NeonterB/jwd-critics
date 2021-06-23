package com.epam.jwd_critics.service;

import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.entity.MovieReview;

import java.util.List;
import java.util.Optional;

public interface MovieReviewService {
    List<MovieReview> getMovieReviewsByMovieId(int movieId, int begin, int end) throws ServiceException;

    List<MovieReview> getMovieReviewsByUserId(int userId, int begin, int end) throws ServiceException;

    int getCountByMovieId(int movieId) throws ServiceException;

    int getCountByUserId(int userId) throws ServiceException;

    Optional<MovieReview> getEntity(int userId, int movieId) throws ServiceException;

    Optional<MovieReview> getEntityById(int reviewId) throws ServiceException;

    int getCount() throws ServiceException;

    MovieReview create(MovieReview review) throws ServiceException;

    void update(MovieReview review) throws ServiceException;

    void delete(int reviewId) throws ServiceException;
}
