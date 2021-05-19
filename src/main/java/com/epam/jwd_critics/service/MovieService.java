package com.epam.jwd_critics.service;

import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<Movie> findAll() throws ServiceException;

    Optional<Movie> findById(Integer id) throws ServiceException;

    public List<Movie> findMoviesByName(String name) throws ServiceException;

    Movie create(Movie movie) throws ServiceException;

    void update(Movie movie) throws ServiceException;

    void delete(Integer id) throws ServiceException;
}
