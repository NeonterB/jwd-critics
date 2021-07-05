package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.exception.DaoException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class AbstractMovieReviewDao extends AbstractBaseDao<Integer, MovieReview> {
    /**
     * Gets {@link List} of movie reviews on movie with given id with limit.
     *
     * @param movieId id of a movie record.
     * @param begin   from.
     * @param end     to.
     * @return list of movie reviews.
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract List<MovieReview> getMovieReviewsByMovieId(Integer movieId, Integer begin, Integer end) throws DaoException;

    /**
     * Gets {@link List} of movie reviews written be user with given id with limit.
     *
     * @param userId id of a user record.
     * @param begin  from.
     * @param end    to.
     * @return list of movie reviews.
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract List<MovieReview> getMovieReviewsByUserId(Integer userId, Integer begin, Integer end) throws DaoException;

    /**
     * Gets count of movie reviews on movie with given id.
     *
     * @param movieId id of a movie record.
     * @return count of movie reviews.
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract int getCountByMovieId(Integer movieId) throws DaoException;

    /**
     * Gets count of movie reviews written by user with given id.
     *
     * @param userId id of a user record.
     * @return count of movie reviews.
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract int getCountByUserId(Integer userId) throws DaoException;

    /**
     * Gets movie review written by user with given id on movie with given id.
     *
     * @param userId  id of a user record.
     * @param movieId id of a movie record.
     * @return {@link Optional} of {@link MovieReview}
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract Optional<MovieReview> getEntity(Integer userId, Integer movieId) throws DaoException;

    /**
     * Checks if movie review written by user with given id on movie with given id is present in database.
     *
     * @param userId  id of a user record.
     * @param movieId id of a movie record.
     * @return true, if movie review record is present id database, false - if not.
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract boolean reviewExists(Integer userId, Integer movieId) throws DaoException;
}
