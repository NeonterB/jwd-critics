package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.entity.Position;
import com.epam.jwd_critics.exception.DaoException;

import java.util.List;
import java.util.Map;

public abstract class AbstractCelebrityDao extends AbstractBaseDao<Integer, Celebrity> {
    public abstract Map<Celebrity, List<Position>> findCrewByMovieId(Integer id) throws DaoException;
}
