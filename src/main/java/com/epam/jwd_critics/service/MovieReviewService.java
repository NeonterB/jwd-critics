package com.epam.jwd_critics.service;

import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.exception.ServiceException;

import java.util.Optional;

public interface MovieReviewService {
    Optional<MovieReview> getEntityById(Integer reviewId) throws ServiceException;

    MovieReview create(MovieReview review) throws ServiceException;

    void update(MovieReview review) throws ServiceException;

    void delete(Integer reviewId) throws ServiceException;
}
