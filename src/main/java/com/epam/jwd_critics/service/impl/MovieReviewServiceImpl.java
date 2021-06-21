package com.epam.jwd_critics.service.impl;

import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.exception.codes.MovieReviewServiceCode;
import com.epam.jwd_critics.dao.AbstractMovieReviewDao;
import com.epam.jwd_critics.dao.EntityTransaction;
import com.epam.jwd_critics.dao.MovieReviewDao;
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.service.MovieReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class MovieReviewServiceImpl implements MovieReviewService {
    private static final Logger logger = LoggerFactory.getLogger(MovieReviewServiceImpl.class);
    private final AbstractMovieReviewDao movieReviewDao = MovieReviewDao.getInstance();

    private MovieReviewServiceImpl() {

    }

    public static MovieReviewServiceImpl getInstance() {
        return MovieReviewServiceImplSingleton.INSTANCE;
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
    public Optional<MovieReview> getEntity(Integer userId, Integer movieId) throws ServiceException {
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
    public List<MovieReview> getMovieReviewsByMovieId(Integer movieId, Integer begin, Integer end) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieReviewDao);
        List<MovieReview> reviews;
        try {
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
    public List<MovieReview> getMovieReviewsByUserId(Integer userId, Integer begin, Integer end) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieReviewDao);
        List<MovieReview> reviews;
        try {
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
    public int getCountByMovieId(Integer movieId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieReviewDao);
        int count;
        try {
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
    public int getCountByUserId(Integer userId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieReviewDao);
        int count;
        try {
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
    public MovieReview create(MovieReview review) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieReviewDao);
        MovieReview createdReview;
        try {
            if (getEntity(review.getUserId(), review.getMovieId()).isPresent()){
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
            logger.info("Celebrity with id {} was updated", review.getId());
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } catch (ServiceException e) {
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
                    .orElseThrow(() -> new ServiceException(MovieReviewServiceCode.REVIEW_DOES_NOT_EXIST));
            movieReviewDao.delete(reviewId);
            transaction.commit();
            logger.info("{} was deleted", reviewToDelete);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } catch (ServiceException e) {
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
