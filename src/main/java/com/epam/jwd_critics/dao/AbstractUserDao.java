package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.DaoException;

import java.util.Optional;

public abstract class AbstractUserDao extends AbstractBaseDao<Integer, User> {
    public abstract Optional<User> getEntityByLogin(String login) throws DaoException;

    public abstract Optional<User> getEntityByEmail(String email) throws DaoException;

    public abstract boolean updatePassword(int id, String password) throws DaoException;

    public abstract boolean loginExists(String login) throws DaoException;

    public abstract boolean emailExists(String email) throws DaoException;

    public abstract boolean insertActivationKey(int userId, String key) throws DaoException;

    public abstract boolean deleteActivationKey(int userId, String key) throws DaoException;

    public abstract boolean insertRecoverKey(int userId, String key) throws DaoException;

    public abstract boolean recoveryKeyExists(int userId) throws DaoException;

    public abstract boolean deleteRecoverKey(int userId, String key) throws DaoException;
}
