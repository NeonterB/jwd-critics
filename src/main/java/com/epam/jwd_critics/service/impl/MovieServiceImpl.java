package com.epam.jwd_critics.service.impl;

import com.epam.jwd_critics.dao.AbstractCelebrityDao;
import com.epam.jwd_critics.dao.AbstractMovieDao;
import com.epam.jwd_critics.dao.AbstractMovieReviewDao;
import com.epam.jwd_critics.dao.CelebrityDao;
import com.epam.jwd_critics.dao.EntityTransaction;
import com.epam.jwd_critics.dao.MovieDao;
import com.epam.jwd_critics.dao.MovieReviewDao;
import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.entity.Genre;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.Position;
import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.exception.codes.CelebrityServiceCode;
import com.epam.jwd_critics.exception.codes.MovieServiceCode;
import com.epam.jwd_critics.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MovieServiceImpl implements MovieService {
    private static final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);
    private final AbstractMovieReviewDao reviewDao = new MovieReviewDao();
    private final AbstractMovieDao movieDao = new MovieDao();
    private final AbstractCelebrityDao celebrityDao = new CelebrityDao();

    private MovieServiceImpl() {

    }

    public static MovieServiceImpl getInstance() {
        return MovieServiceImplSingleton.INSTANCE;
    }

    @Override
    public List<Movie> getAllBetween(int begin, int end) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao);
        List<Movie> movies;
        try {
            movies = movieDao.getAllBetween(begin, end);
            for (Movie movie : movies) {
                updateInfo(movie);
            }
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return movies;
    }

    @Override
    public int getCount() throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao);
        int count;
        try {
            count = movieDao.getCount();
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
    public Optional<Movie> getEntityById(int id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao);
        Optional<Movie> movie;
        try {
            movie = movieDao.getEntityById(id);
            if (movie.isPresent()) {
                updateInfo(movie.get());
            }
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
    public List<Movie> getMoviesByName(String name) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao);
        List<Movie> movies;
        try {
            movies = movieDao.getMoviesByName(name);
            for (Movie movie : movies) {
                updateInfo(movie);
            }
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return movies;
    }

    @Override
    public void addGenre(int movieId, Genre genre) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao);
        try {
            if (!movieDao.idExists(movieId))
                throw new ServiceException(MovieServiceCode.MOVIE_DOES_NOT_EXIST);
            movieDao.addGenre(movieId, genre);
            logger.info("Genre {} was added to movie with id {}", genre, movieId);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    @Override
    public void removeGenre(int movieId, Genre genre) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao);
        try {
            if (!movieDao.idExists(movieId))
                throw new ServiceException(MovieServiceCode.MOVIE_DOES_NOT_EXIST);
            movieDao.removeGenre(movieId, genre);
            logger.info("Genre {} was removed from movie with id {}", genre, movieId);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    @Override
    public void assignCelebrityOnPosition(int movieId, int celebrityId, Position position) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao);
        try {
            if (!movieDao.idExists(movieId))
                throw new ServiceException(MovieServiceCode.MOVIE_DOES_NOT_EXIST);
            if (!celebrityDao.idExists(celebrityId))
                throw new ServiceException(CelebrityServiceCode.CELEBRITY_DOES_NOT_EXIST);
            movieDao.assignCelebrityOnPosition(movieId, celebrityId, position);
            logger.info("Celebrity with id {} was added to movie with id {} on position {}", celebrityId, movieId, position);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    @Override
    public void removeCelebrityFromPosition(int movieId, int celebrityId, Position position) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao, celebrityDao);
        try {
            if (!movieDao.idExists(movieId))
                throw new ServiceException(MovieServiceCode.MOVIE_DOES_NOT_EXIST);
            if (!celebrityDao.idExists(celebrityId))
                throw new ServiceException(CelebrityServiceCode.CELEBRITY_DOES_NOT_EXIST);
            movieDao.removeCelebrityFromPosition(movieId, celebrityId, position);
            logger.info("Celebrity with id {} was removed from movie with id {} from position {}", celebrityId, movieId, position);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    @Override
    public Movie create(Movie movie) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao);
        Movie createdMovie;
        try {
            createdMovie = movieDao.create(movie);
            for (Genre genre : movie.getGenres()) {
                movieDao.addGenre(movie.getId(), genre);
            }
            transaction.commit();
            logger.info("{} created", createdMovie);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return createdMovie;
    }

    @Override
    public void update(Movie movie) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao);
        try {
            if (!movieDao.idExists(movie.getId())) {
                throw new ServiceException(MovieServiceCode.MOVIE_DOES_NOT_EXIST);
            }
            movieDao.update(movie);
            transaction.commit();
            logger.info("Movie with id {} was updated", movie.getId());
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
    public void delete(int id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao, reviewDao);
        try {
            if (!movieDao.idExists(id)) {
                throw new ServiceException(MovieServiceCode.MOVIE_DOES_NOT_EXIST);
            }
            movieDao.delete(id);
            transaction.commit();
            logger.info("Movie with id {} was deleted", id);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    private void updateInfo(Movie movie) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(celebrityDao, reviewDao);
        try {
            List<Genre> genres = movieDao.getMovieGenresById(movie.getId());
            Map<Position, List<Celebrity>> crew = celebrityDao.getStaffByMovieId(movie.getId());
            movie.setGenres(genres);
            movie.setStaff(crew);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    private static class MovieServiceImplSingleton {
        private static final MovieServiceImpl INSTANCE = new MovieServiceImpl();
    }
}
