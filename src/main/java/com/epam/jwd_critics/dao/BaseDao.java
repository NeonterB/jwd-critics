package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.BaseEntity;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface BaseDao<K, T extends BaseEntity> {
    List<T> findAll() throws DaoException;

    Optional<T> getEntityById(K id) throws DaoException;

    void deleteEntityById(K id) throws DaoException;

    User create(T user) throws DaoException;

    void update(T user) throws DaoException;
}
