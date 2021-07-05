package com.epam.jwd_critics.service.impl;

import com.epam.jwd_critics.dao.AbstractMovieDao;
import com.epam.jwd_critics.dao.AbstractMovieReviewDao;
import com.epam.jwd_critics.dao.AbstractUserDao;
import com.epam.jwd_critics.dao.EntityTransaction;
import com.epam.jwd_critics.dao.MovieDao;
import com.epam.jwd_critics.dao.MovieReviewDao;
import com.epam.jwd_critics.dao.UserDao;
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.exception.codes.MovieReviewServiceCode;
import com.epam.jwd_critics.exception.codes.MovieServiceCode;
import com.epam.jwd_critics.exception.codes.UserServiceCode;
import com.epam.jwd_critics.service.MovieReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class MovieReviewServiceImpl implements MovieReviewService {
    private static final Logger logger = LoggerFactory.getLogger(MovieReviewServiceImpl.class);
    private final AbstractMovieReviewDao movieReviewDao = new MovieReviewDao();
    private final AbstractMovieDao movieDao = new MovieDao();
    private final AbstractUserDao userDao = new UserDao();

    private MovieReviewServiceImpl() {

    }

    public static MovieReviewServiceImpl getInstance() {
        return MovieReviewServiceImplSingleton.INSTANCE;
    }

    @Override
    public List<MovieReview> getMovieReviewsByMovieId(int movieId, int begin, int end) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieReviewDao, movieDao);
        List<MovieReview> reviews;
        try {
            if (!movieDao.idExists(movieId)) {
                throw new ServiceException(MovieServiceCode.MOVIE_DOES_NOT_EXIST);
            }
            reviews = movieReviewDao.getMovieReviewsByMovieId(movieId, begin, end);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return reviews;
    }

    @Override
    public List<MovieReview> getMovieReviewsByUserId(int userId, int begin, int end) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieReviewDao, userDao);
        List<MovieReview> reviews;
        try {
            if (!userDao.idExists(userId)) {
                throw new ServiceException(UserServiceCode.USER_DOES_NOT_EXIST);
            }
            reviews = movieReviewDao.getMovieReviewsByUserId(userId, begin, end);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return reviews;
    }

    @Override
    public int getCountByMovieId(int movieId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieReviewDao, movieDao);
        int count;
        try {
            if (!movieDao.idExists(movieId)) {
                throw new ServiceException(MovieServiceCode.MOVIE_DOES_NOT_EXIST);
            }
            count = movieReviewDao.getCountByMovieId(movieId);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return count;
    }

    @Override
    public int getCountByUserId(int userId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieReviewDao, userDao);
        int count;
        try {
            if (!userDao.idExists(userId)) {
                throw new ServiceException(UserServiceCode.USER_DOES_NOT_EXIST);
            }
            count = movieReviewDao.getCountByUserId(userId);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return count;
    }

    @Override
    public Optional<MovieReview> getEntity(int userId, int movieId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieReviewDao);
        Optional<MovieReview> movieReview;
        try {
            movieReview = movieReviewDao.getEntity(userId, movieId);
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
    public Optional<MovieReview> getEntityById(int reviewId) throws ServiceException {
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
    public int getCount() throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieReviewDao);
        int count;
        try {
            count = movieReviewDao.getCount();
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return count;
    }

    @Override
    public MovieReview create(MovieReview review) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieReviewDao);
        MovieReview createdReview;
        try {
            if (movieReviewDao.reviewExists(review.getUserId(), review.getMovieId())) {
                throw new ServiceException(MovieReviewServiceCode.USER_ALREADY_LEFT_A_REVIEW);
            }
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
                throw new ServiceException(MovieReviewServiceCode.REVIEW_DOES_NOT_EXIST);
            }
            movieReviewDao.update(review);
            transaction.commit();
            logger.info("Review with id {} was updated", review.getId());
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    @Override
    public void delete(int reviewId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieReviewDao);
        try {
            if (!movieReviewDao.idExists(reviewId)) {
                throw new ServiceException(MovieReviewServiceCode.REVIEW_DOES_NOT_EXIST);
            }
            movieReviewDao.delete(reviewId);
            transaction.commit();
            logger.info("Review with id {} was deleted", reviewId);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    private static class MovieReviewServiceImplSingleton {
        private static final MovieReviewServiceImpl INSTANCE = new MovieReviewServiceImpl();
    }
}
