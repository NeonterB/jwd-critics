package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.entity.Position;
import com.epam.jwd_critics.exception.DaoException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractCelebrityDao extends AbstractBaseDao<Integer, Celebrity> {
    /**
     * Finds all movie staff and puts them in a map.
     *
     * @param movieId id of movie record.
     * @return movie staff.
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract Map<Position, List<Celebrity>> getStaffByMovieId(Integer movieId) throws DaoException;

    /**
     * Checks if celebrity record with given credentials is present in database.
     *
     * @param firstName first name of celebrity.
     * @param lastName  last name of celebrity.
     * @return true, if celebrity record is present id database, false - if not.
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract boolean celebrityExists(String firstName, String lastName) throws DaoException;

    /**
     * Finds celebrity record with given credentials in database.
     *
     * @param firstName first name of celebrity.
     * @param lastName  last name of celebrity.
     * @return {@link Optional} of celebrity.
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract Optional<Celebrity> getEntityByFullName(String firstName, String lastName) throws DaoException;
}
