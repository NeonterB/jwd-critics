package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.Position;
import com.epam.jwd_critics.exception.DaoException;

import java.net.Inet4Address;
import java.util.List;
import java.util.Map;

public abstract class AbstractCelebrityDao extends AbstractBaseDao<Integer, Celebrity> {
    public abstract Map<Celebrity, List<Position>> findCrewByMovieId(Integer movieId) throws DaoException;
}
