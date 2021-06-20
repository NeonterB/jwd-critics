package com.epam.jwd_critics.model.service;

import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.model.entity.MovieReview;

import java.util.List;
import java.util.Optional;

public interface MovieReviewService {
    List<MovieReview> getMovieReviewsByMovieId(Integer movieId, Integer begin, Integer end) throws ServiceException;

    List<MovieReview> getMovieReviewsByUserId(Integer userId, Integer begin, Integer end) throws ServiceException;

    int getCountByMovieId(Integer movieId) throws ServiceException;

    int getCountByUserId(Integer userId) throws ServiceException;

    Optional<MovieReview> getEntity(Integer userId, Integer movieId) throws ServiceException;

    Optional<MovieReview> getEntityById(Integer reviewId) throws ServiceException;

    int getCount() throws ServiceException;

    MovieReview create(MovieReview review) throws ServiceException;

    void update(MovieReview review) throws ServiceException;

    void delete(Integer reviewId) throws ServiceException;
}
