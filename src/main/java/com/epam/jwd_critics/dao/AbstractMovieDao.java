package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.Genre;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.Position;
import com.epam.jwd_critics.exception.DaoException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class AbstractMovieDao extends AbstractBaseDao<Integer, Movie> {
    /**
     * Finds all movies and positions, to witch celebrity with given id was assigned, and puts them in a map
     *
     * @param celebrityId id of a celebrity record.
     * @return celebrity jobs.
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract Map<Movie, List<Position>> getJobsByCelebrityId(Integer celebrityId) throws DaoException;

    /**
     * Finds all genres of movie with given id..
     *
     * @param movieId id of a movie record.
     * @return movie genres.
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract List<Genre> getMovieGenresById(Integer movieId) throws DaoException;

    /**
     * Adds new genre to a movie with given id
     *
     * @param movieId id of a movie record.
     * @param genre   genre to add.
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract void addGenre(Integer movieId, Genre genre) throws DaoException;

    /**
     * Removes genre from movie with given id.
     *
     * @param movieId id of a movie record.
     * @param genre   genre to remove.
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract void removeGenre(Integer movieId, Genre genre) throws DaoException;

    /**
     * Assigns celebrity with given id on given position in movie with given id.
     *
     * @param movieId     id of a movie record.
     * @param celebrityId id of a celebrity record.
     * @param position    position to assign celebrity on.
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract void assignCelebrityOnPosition(Integer movieId, Integer celebrityId, Position position) throws DaoException;

    /**
     * Removes celebrity with given id from given position in movie with given id.
     *
     * @param movieId     id of a movie record.
     * @param celebrityId id of a celebrity record.
     * @param position    position to remove celebrity from.
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract void removeCelebrityFromPosition(Integer movieId, Integer celebrityId, Position position) throws DaoException;

    /**
     * Gets {@link List} of movies with given name part.
     *
     * @param namePart part of movie name.
     * @return list of movies.
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract List<Movie> getMoviesByNamePart(String namePart) throws DaoException;
}
