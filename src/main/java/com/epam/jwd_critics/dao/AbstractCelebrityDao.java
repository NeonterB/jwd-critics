package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.entity.Position;
import com.epam.jwd_critics.exception.DaoException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractCelebrityDao extends AbstractBaseDao<Integer, Celebrity> {
    public abstract Map<Position, List<Celebrity>> getStaffByMovieId(Integer movieId) throws DaoException;

    public abstract boolean celebrityExists(String firstName, String lastName) throws DaoException;

    public abstract Optional<Celebrity> getEntityByFullName(String firstName, String lastName) throws DaoException;
}
