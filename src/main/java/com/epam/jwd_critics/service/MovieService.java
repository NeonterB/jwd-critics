package com.epam.jwd_critics.service;

import com.epam.jwd_critics.entity.Genre;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.Position;
import com.epam.jwd_critics.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<Movie> getAllBetween(int begin, int end) throws ServiceException;

    int getCount() throws ServiceException;

    Optional<Movie> getEntityById(int id) throws ServiceException;

    List<Movie> getMoviesByNamePart(String namePart) throws ServiceException;

    void addGenre(int movieId, Genre genre) throws ServiceException;

    void removeGenre(int movieId, Genre genre) throws ServiceException;

    void assignCelebrityOnPosition(int movieId, int celebrityId, Position position) throws ServiceException;

    void removeCelebrityFromPosition(int movieId, int celebrityId, Position position) throws ServiceException;

    Movie create(Movie movie) throws ServiceException;

    void update(Movie movie) throws ServiceException;

    void delete(int id) throws ServiceException;
}
