package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.exception.DaoException;

import java.util.List;

public abstract class AbstractMovieReviewDao extends AbstractBaseDao<Integer, MovieReview> {
    public abstract List<MovieReview> getMovieReviewsByMovieId(Integer movieId) throws DaoException;

    public abstract List<MovieReview> getMovieReviewsByUserId(Integer userId) throws DaoException;
}
