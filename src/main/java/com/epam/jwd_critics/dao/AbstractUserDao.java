package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.DaoException;

import java.util.Optional;

public abstract class AbstractUserDao extends AbstractBaseDao<Integer, User> {
    public abstract Optional<User> getEntityByLogin(String login) throws DaoException;

    public abstract Optional<User> getEntityByEmail(String email) throws DaoException;

    public abstract boolean updatePassword(Integer id, String password) throws DaoException;

    public abstract boolean loginExists(String login) throws DaoException;

    public abstract boolean emailExists(String email) throws DaoException;

    public abstract boolean insertActivationKey(Integer userId, String key) throws DaoException;

    public abstract Optional<String> selectActivationKey(Integer userId) throws DaoException;

    public abstract void deleteActivationKey(Integer userId) throws DaoException;

    public abstract boolean insertRecoveryKey(Integer userId, String key) throws DaoException;

    public abstract Optional<String> selectRecoveryKey(Integer userId) throws DaoException;

    public abstract void deleteRecoveryKey(Integer userId) throws DaoException;
}
