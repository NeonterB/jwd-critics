package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.Genre;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.Position;
import com.epam.jwd_critics.exception.DaoException;

import java.util.List;
import java.util.Map;

public abstract class AbstractMovieDao extends AbstractBaseDao<Integer, Movie> {
    public abstract Map<Movie, List<Position>> findMoviesByCelebrityId(Integer celebrityId) throws DaoException;

    public abstract List<Genre> getMovieGenresById(Integer movieId) throws DaoException;
}
