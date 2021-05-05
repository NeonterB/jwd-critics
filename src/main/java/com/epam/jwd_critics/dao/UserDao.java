package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> findAll() throws DaoException;

    Optional<User> get(Integer id) throws DaoException;

    boolean delete(Integer id) throws DaoException;

    boolean delete(String login) throws DaoException;

    User create(User user) throws DaoException;

    void update(User user) throws DaoException;
}
