package com.epam.jwd_critics.model.service.impl;

import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.exception.MovieReviewServiceException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.exception.codes.MovieReviewServiceCode;
import com.epam.jwd_critics.model.dao.AbstractMovieDao;
import com.epam.jwd_critics.model.dao.AbstractMovieReviewDao;
import com.epam.jwd_critics.model.dao.AbstractUserDao;
import com.epam.jwd_critics.model.dao.EntityTransaction;
import com.epam.jwd_critics.model.dao.MovieDao;
import com.epam.jwd_critics.model.dao.MovieReviewDao;
import com.epam.jwd_critics.model.dao.UserDao;
import com.epam.jwd_critics.model.entity.MovieReview;
import com.epam.jwd_critics.model.service.MovieReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class MovieReviewServiceImpl implements MovieReviewService {
    private static final Logger logger = LoggerFactory.getLogger(MovieReviewServiceImpl.class);
    private final AbstractMovieReviewDao movieReviewDao = MovieReviewDao.getInstance();
    private final AbstractMovieDao movieDao = MovieDao.getInstance();
    private final AbstractUserDao userDao = UserDao.getInstance();

    private MovieReviewServiceImpl() {

    }

    public static MovieReviewServiceImpl getInstance() {
        return MovieReviewServiceImpl.MovieReviewServiceImplSingleton.INSTANCE;
    }

    @Override
    public Optional<MovieReview> getEntityById(Integer reviewId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieReviewDao);
        Optional<MovieReview> movieReview;
        try {
            movieReview = movieReviewDao.getEntityById(reviewId);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return movieReview;
    }

    @Override
    public MovieReview create(MovieReview review) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieReviewDao);
        MovieReview createdReview;
        try {
            createdReview = movieReviewDao.create(review);
            transaction.commit();
            logger.info("{} created", createdReview);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return createdReview;
    }

    @Override
    public void update(MovieReview review) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieReviewDao);
        try {
            if (!movieReviewDao.idExists(review.getId())) {
                throw new MovieReviewServiceException(MovieReviewServiceCode.REVIEW_DOES_NOT_EXIST);
            }
            movieReviewDao.update(review);
            transaction.commit();
            logger.info("Celebrity with id {} was updated", review.getId());
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } catch (MovieReviewServiceException e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
    }

    @Override
    public void delete(Integer reviewId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieReviewDao);
        try {
            MovieReview reviewToDelete = movieReviewDao.getEntityById(reviewId)
                    .orElseThrow(() -> new MovieReviewServiceException(MovieReviewServiceCode.REVIEW_DOES_NOT_EXIST));
            movieReviewDao.delete(reviewId);
            transaction.commit();
            logger.info("{} was deleted", reviewToDelete);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } catch (MovieReviewServiceException e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
    }

    private static class MovieReviewServiceImplSingleton {
        private static final MovieReviewServiceImpl INSTANCE = new MovieReviewServiceImpl();
    }
}
