package com.epam.jwd_critics.service;

import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface MovieReviewService {
    /**
     * Gets {@link List} of movie reviews on movie with given id with limit.
     *
     * @param movieId id of a movie record.
     * @param begin   from.
     * @param end     to.
     * @return list of movie reviews.
     * @throws ServiceException if movie with given id does not exist or {@link DaoException} was caught.
     */
    List<MovieReview> getMovieReviewsByMovieId(int movieId, int begin, int end) throws ServiceException;

    /**
     * Gets {@link List} of movie reviews written be user with given id with limit.
     *
     * @param userId id of a user record.
     * @param begin  from.
     * @param end    to.
     * @return list of movie reviews.
     * @throws ServiceException if user with given id does not exist or {@link DaoException} was caught.
     */
    List<MovieReview> getMovieReviewsByUserId(int userId, int begin, int end) throws ServiceException;

    /**
     * Gets count of movie reviews on movie with given id.
     *
     * @param movieId id of a movie record.
     * @return count of movie reviews.
     * @throws ServiceException if movie with given id does not exist or {@link DaoException} was caught.
     */
    int getCountByMovieId(int movieId) throws ServiceException;

    /**
     * Gets count of movie reviews written by user with given id.
     *
     * @param userId id of a user record.
     * @return count of movie reviews.
     * @throws ServiceException if user with given id does not exist or {@link DaoException} was caught.
     */
    int getCountByUserId(int userId) throws ServiceException;

    /**
     * Gets movie review written by user with given id on movie with given id.
     *
     * @param userId  id of a user record.
     * @param movieId id of a movie record.
     * @return {@link Optional} of {@link MovieReview}
     * @throws ServiceException if {@link DaoException} was caught.
     */
    Optional<MovieReview> getEntity(int userId, int movieId) throws ServiceException;

    /**
     * Gets movie review record with given id.
     *
     * @param reviewId id of a movie review.
     * @return {@link Optional} of movie review.
     * @throws ServiceException if {@link DaoException} was caught.
     */
    Optional<MovieReview> getEntityById(int reviewId) throws ServiceException;

    /**
     * Gets count of movie reviews.
     *
     * @return count of movie reviews.
     * @throws ServiceException if {@link DaoException} was caught.
     */
    int getCount() throws ServiceException;

    /**
     * Creates new movie review.
     *
     * @param review movie review to create.
     * @throws ServiceException if given movie review already exists or {@link DaoException} was caught.
     */
    MovieReview create(MovieReview review) throws ServiceException;

    /**
     * Finds and updates movie review.
     *
     * @param review movie review to update.
     * @throws ServiceException if given movie review does not exist or {@link DaoException} was caught.
     */
    void update(MovieReview review) throws ServiceException;

    /**
     * Deletes movie review with given id.
     *
     * @param reviewId id of a movie review.
     * @throws ServiceException if given movie review does not exist or {@link DaoException} was caught.
     */
    void delete(int reviewId) throws ServiceException;
}
