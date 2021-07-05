package com.epam.jwd_critics.service;

import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface CelebrityService {
    /**
     * Gets all celebrities with limit.
     *
     * @param begin from.
     * @param end   to.
     * @return {@link List} of celebrities.
     * @throws ServiceException if {@link DaoException} was caught.
     */
    List<Celebrity> getAllBetween(int begin, int end) throws ServiceException;

    /**
     * Gets count of celebrities.
     *
     * @return count of celebrities.
     * @throws ServiceException if {@link DaoException} was caught.
     */
    int getCount() throws ServiceException;

    /**
     * Finds celebrity with given id.
     *
     * @param id celebrity id.
     * @return {@link Optional} of celebrity.
     * @throws ServiceException if {@link DaoException} was caught.
     */
    Optional<Celebrity> getEntityById(int id) throws ServiceException;

    /**
     * Finds celebrity with given credentials.
     *
     * @param firstName first name of celebrity.
     * @param lastName  last name of celebrity.
     * @return {@link Optional} of celebrity.
     * @throws ServiceException if {@link DaoException} was caught.
     */
    Optional<Celebrity> getEntityByFullName(String firstName, String lastName) throws ServiceException;

    /**
     * Creates new celebrity.
     *
     * @param celebrity celebrity to create.
     * @throws ServiceException if given celebrity already exists or {@link DaoException} was caught.
     */
    Celebrity create(Celebrity celebrity) throws ServiceException;

    /**
     * Finds and updates celebrity.
     *
     * @param celebrity celebrity to update.
     * @throws ServiceException if given celebrity does not exist or {@link DaoException} was caught.
     */
    void update(Celebrity celebrity) throws ServiceException;

    /**
     * Deletes celebrity with given id.
     *
     * @param id id of a celebrity.
     * @throws ServiceException if given celebrity does not exist or {@link DaoException} was caught.
     */
    void delete(int id) throws ServiceException;
}
