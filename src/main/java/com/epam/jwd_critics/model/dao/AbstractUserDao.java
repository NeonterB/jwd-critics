package com.epam.jwd_critics.model.dao;

import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.model.entity.User;

import java.util.Optional;

public abstract class AbstractUserDao extends AbstractBaseDao<Integer, User> {
    public abstract Optional<User> getEntityByLogin(String login) throws DaoException;

    public abstract boolean loginExists(String login) throws DaoException;
}
