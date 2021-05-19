package com.epam.jwd_critics.service.impl;

import com.epam.jwd_critics.dao.AbstractMovieDao;
import com.epam.jwd_critics.dao.AbstractMovieReviewDao;
import com.epam.jwd_critics.dao.AbstractUserDao;
import com.epam.jwd_critics.dao.EntityTransaction;
import com.epam.jwd_critics.dao.MovieDao;
import com.epam.jwd_critics.dao.MovieReviewDao;
import com.epam.jwd_critics.dao.UserDao;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.exception.MovieServiceException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.exception.codes.MovieServiceCode;
import com.epam.jwd_critics.service.MovieService;
import com.epam.jwd_critics.util.PasswordAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class MovieServiceImpl implements MovieService {
    private static final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);
    private final AbstractMovieReviewDao reviewDao = MovieReviewDao.getInstance();
    private final AbstractUserDao userDao = UserDao.getInstance();
    private final AbstractMovieDao movieDao = MovieDao.getInstance();
    private final PasswordAuthenticator passwordAuthenticator = new PasswordAuthenticator();

    private MovieServiceImpl() {

    }

    public static MovieServiceImpl getInstance() {
        return MovieServiceImpl.MovieServiceImplSingleton.INSTANCE;
    }

    private static class MovieServiceImplSingleton {
        private static final MovieServiceImpl INSTANCE = new MovieServiceImpl();
    }

    @Override
    public List<Movie> findAll() throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao);
        List<Movie> movieList;
        try {
            movieList = movieDao.findAll();
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return movieList;
    }

    @Override
    public Optional<Movie> findById(Integer id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao);
        Optional<Movie> movie;
        try {
            movie = movieDao.findEntityById(id);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return movie;
    }

    @Override
    public Optional<Movie> create(Movie movie) throws ServiceException {
        return Optional.empty();
    }

    @Override
    public void update(Movie movie) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao);
        try {
            if (!movieDao.idExists(movie.getId())) {
                throw new MovieServiceException(MovieServiceCode.MOVIE_DOES_NOT_EXIST);
            }
            movieDao.update(movie);
            transaction.commit();
            logger.info("Movie with id {} was updated", movie.getId());
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } catch (MovieServiceException e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {

    }

    @Override
    public Optional<Movie> findByName(String title) throws ServiceException {
        return Optional.empty();
    }
}
