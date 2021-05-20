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
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.entity.Position;
import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.exception.MovieServiceException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.exception.codes.MovieServiceCode;
import com.epam.jwd_critics.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MovieServiceImpl implements MovieService {
    private static final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);
    private final AbstractMovieReviewDao reviewDao = MovieReviewDao.getInstance();
    private final AbstractMovieDao movieDao = MovieDao.getInstance();
    private final AbstractCelebrityDao celebrityDao = CelebrityDao.getInstance();

    private MovieServiceImpl() {

    }

    public static MovieServiceImpl getInstance() {
        return MovieServiceImpl.MovieServiceImplSingleton.INSTANCE;
    }

    @Override
    public List<Movie> getAll() throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao);
        List<Movie> movies;
        try {
            movies = movieDao.getAll();
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
    public Optional<Movie> getEntityById(Integer id) throws ServiceException {
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
    public void addGenre(Integer movieId, Genre genre) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao);
        try {
            if (!movieDao.idExists(movieId))
                throw new MovieServiceException(MovieServiceCode.MOVIE_DOES_NOT_EXIST);
            movieDao.addGenre(movieId, genre);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    @Override
    public void removeGenre(Integer movieId, Genre genre) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao);
        try {
            if (!movieDao.idExists(movieId))
                throw new MovieServiceException(MovieServiceCode.MOVIE_DOES_NOT_EXIST);
            movieDao.removeGenre(movieId, genre);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    @Override
    public void addCelebrityAndPosition(Integer movieId, Integer celebrityId, Position position) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao);
        try {
            if (!movieDao.idExists(movieId))
                throw new MovieServiceException(MovieServiceCode.MOVIE_DOES_NOT_EXIST);
            movieDao.addStaffAndPosition(movieId, celebrityId, position);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    @Override
    public void removeCelebrityAndPosition(Integer movieId, Integer celebrityId, Position position) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao);
        try {
            if (!movieDao.idExists(movieId))
                throw new MovieServiceException(MovieServiceCode.MOVIE_DOES_NOT_EXIST);
            movieDao.removeStaffAndPosition(movieId, celebrityId, position);
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
        EntityTransaction transaction = new EntityTransaction(movieDao, reviewDao);
        try {
            Movie movieToDelete = movieDao.getEntityById(id)
                    .orElseThrow(() -> new MovieServiceException(MovieServiceCode.MOVIE_DOES_NOT_EXIST));
            movieDao.delete(id);
            transaction.commit();
            logger.info("{} was deleted", movieToDelete);
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

    private void updateInfo(Movie movie) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(celebrityDao, reviewDao);
        try {
            List<Genre> genres = movieDao.getMovieGenresById(movie.getId());
            Map<Celebrity, List<Position>> crew = celebrityDao.getStaffByMovieId(movie.getId());
            List<MovieReview> reviews = reviewDao.getMovieReviewsByMovieId(movie.getId());
            movie.setGenres(genres);
            movie.setStaff(crew);
            movie.setReviews(reviews);
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
