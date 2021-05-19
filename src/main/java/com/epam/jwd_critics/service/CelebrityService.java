package com.epam.jwd_critics.service;

import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface CelebrityService {
    List<Celebrity> getAll() throws ServiceException;

    Optional<Celebrity> getEntityById(Integer id) throws ServiceException;

    void update(Celebrity celebrity) throws ServiceException;

    Celebrity create(Celebrity celebrity) throws ServiceException;

    void delete(Integer id) throws ServiceException;
}
