package com.epam.jwd_critics.model.dao;

import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.model.entity.Celebrity;
import com.epam.jwd_critics.model.entity.Position;

import java.util.List;
import java.util.Map;

public abstract class AbstractCelebrityDao extends AbstractBaseDao<Integer, Celebrity> {
    public abstract Map<Position, List<Celebrity>> getStaffByMovieId(Integer movieId) throws DaoException;
}
