package com.epam.jwd_critics.model.service;

import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.model.entity.Genre;
import com.epam.jwd_critics.model.entity.Movie;
import com.epam.jwd_critics.model.entity.Position;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<Movie> getAll() throws ServiceException;

    Optional<Movie> getEntityById(Integer id) throws ServiceException;

    List<Movie> getMoviesByName(String name) throws ServiceException;

    void addGenre(Integer movieId, Genre genre) throws ServiceException;

    void removeGenre(Integer movieId, Genre genre) throws ServiceException;

    void addCelebrityAndPosition(Integer movieId, Integer celebrityId, Position position) throws ServiceException;

    void removeCelebrityAndPosition(Integer movieId, Integer celebrityId, Position position) throws ServiceException;

    Movie create(Movie movie) throws ServiceException;

    void update(Movie movie) throws ServiceException;

    void delete(Integer id) throws ServiceException;
}
