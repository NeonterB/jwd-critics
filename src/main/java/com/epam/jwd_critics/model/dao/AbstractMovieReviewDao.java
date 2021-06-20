package com.epam.jwd_critics.model.dao;

import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.model.entity.MovieReview;

import java.util.List;

public abstract class AbstractMovieReviewDao extends AbstractBaseDao<Integer, MovieReview> {
    public abstract List<MovieReview> getMovieReviewsByMovieId(Integer movieId, Integer begin, Integer end) throws DaoException;

    public abstract List<MovieReview> getMovieReviewsByUserId(Integer userId, Integer begin, Integer end) throws DaoException;

    public abstract int getCountByMovieId(Integer movieId) throws DaoException;

    public abstract int getCountByUserId(Integer userId) throws DaoException;
}
