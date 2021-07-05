package com.epam.jwd_critics.service;

import com.epam.jwd_critics.entity.Genre;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.Position;
import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    /**
     * Gets all movies with limit.
     *
     * @param begin from.
     * @param end   to.
     * @return {@link List} of movies.
     * @throws ServiceException if {@link DaoException} was caught.
     */
    List<Movie> getAllBetween(int begin, int end) throws ServiceException;

    /**
     * Gets count of movies.
     *
     * @return count of movies.
     * @throws ServiceException if {@link DaoException} was caught.
     */
    int getCount() throws ServiceException;

    /**
     * Finds movie with given id.
     *
     * @param id movie id.
     * @return {@link Optional} of movie.
     * @throws ServiceException if {@link DaoException} was caught.
     */
    Optional<Movie> getEntityById(int id) throws ServiceException;

    /**
     * Finds {@link List} of movies with given part in name.
     *
     * @param namePart name part.
     * @return {@link List} of movies.
     * @throws ServiceException if {@link DaoException} was caught.
     */
    List<Movie> getMoviesByNamePart(String namePart) throws ServiceException;

    /**
     * Adds new genre to a movie with given id
     *
     * @param movieId id of a movie record.
     * @param genre   genre to add.
     * @throws ServiceException if movie with given id does not exist or {@link DaoException} was caught.
     */
    void addGenre(int movieId, Genre genre) throws ServiceException;

    /**
     * Removes genre from movie with given id.
     *
     * @param movieId id of a movie record.
     * @param genre   genre to remove.
     * @throws ServiceException if movie with given id does not exist or {@link DaoException} was caught.
     */
    void removeGenre(int movieId, Genre genre) throws ServiceException;

    /**
     * Assigns celebrity with given id on given position in movie with given id.
     *
     * @param movieId     id of a movie record.
     * @param celebrityId id of a celebrity record.
     * @param position    position to assign celebrity on.
     * @throws ServiceException if movie with given id does not exist, if celebrity with given id does not exist or if {@link DaoException} was caught.
     */
    void assignCelebrityOnPosition(int movieId, int celebrityId, Position position) throws ServiceException;

    /**
     * Removes celebrity with given id from given position in movie with given id.
     *
     * @param movieId     id of a movie record.
     * @param celebrityId id of a celebrity record.
     * @param position    position to remove celebrity from.
     * @throws ServiceException if movie with given id does not exist, if celebrity with given id does not exist or if {@link DaoException} was caught.
     */
    void removeCelebrityFromPosition(int movieId, int celebrityId, Position position) throws ServiceException;

    /**
     * Creates new movie.
     *
     * @param movie movie to create.
     * @throws ServiceException if given movie already exists or {@link DaoException} was caught.
     */
    Movie create(Movie movie) throws ServiceException;

    /**
     * Finds and updates movie.
     *
     * @param movie movie to update.
     * @throws ServiceException if given movie does not exist or {@link DaoException} was caught.
     */
    void update(Movie movie) throws ServiceException;

    /**
     * Deletes movie with given id.
     *
     * @param id id of a movie.
     * @throws ServiceException if given movie does not exist or {@link DaoException} was caught.
     */
    void delete(int id) throws ServiceException;
}
