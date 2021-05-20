package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.DaoException;

import java.util.Optional;

public abstract class AbstractUserDao extends AbstractBaseDao<Integer, User> {
    public abstract Optional<User> getEntityByLogin(String login) throws DaoException;

    public abstract boolean loginExists(String login) throws DaoException;
}
