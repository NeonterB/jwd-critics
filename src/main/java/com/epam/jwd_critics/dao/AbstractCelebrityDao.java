package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.entity.Position;

import java.util.List;
import java.util.Map;

public abstract class AbstractCelebrityDao extends AbstractBaseDao<Integer, Celebrity> {
    public abstract Map<Position, List<Celebrity>> getStaffByMovieId(Integer movieId) throws DaoException;
}
