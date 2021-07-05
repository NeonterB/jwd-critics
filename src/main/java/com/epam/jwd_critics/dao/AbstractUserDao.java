package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.DaoException;

import java.sql.SQLException;
import java.util.Optional;

public abstract class AbstractUserDao extends AbstractBaseDao<Integer, User> {
    /**
     * Finds {@link User} by login.
     *
     * @param login login of a user record.
     * @return {@link Optional} of user.
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract Optional<User> getEntityByLogin(String login) throws DaoException;

    /**
     * Finds {@link User} by email.
     *
     * @param email email of a user record.
     * @return {@link Optional} of user.
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract Optional<User> getEntityByEmail(String email) throws DaoException;

    /**
     * Updates password of user with given id.
     *
     * @param id       id of a user record
     * @param password new password value
     * @return true, if password was updated successfully, false - of not
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract void updatePassword(Integer id, String password) throws DaoException;

    /**
     * Checks if user record with given login is present in database.
     *
     * @param login login of a user record.
     * @return true, if record with given login is present in database, false - if not.
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract boolean loginExists(String login) throws DaoException;

    /**
     * Checks if user record with given email is present in database.
     *
     * @param email login of a user record.
     * @return true, if record with given email is present in database, false - if not.
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract boolean emailExists(String email) throws DaoException;

    /**
     * Inserts new record in a user_activation_key table
     *
     * @param userId id of a user
     * @param key    activation key
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract void insertActivationKey(Integer userId, String key) throws DaoException;

    /**
     * Finds an activation key for user with given id.
     *
     * @param userId id of a user.
     * @return {@link Optional} of activation key
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract Optional<String> selectActivationKey(Integer userId) throws DaoException;

    /**
     * Deletes record from a user_activation_key table
     *
     * @param userId id of a user
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract void deleteActivationKey(Integer userId) throws DaoException;

    /**
     * Inserts new record in a user_recovery_key table
     *
     * @param userId id of a user
     * @param key    recovery key
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract void insertRecoveryKey(Integer userId, String key) throws DaoException;

    /**
     * Finds an recovery key for user with given id.
     *
     * @param userId id of a user.
     * @return {@link Optional} of recovery key
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract Optional<String> selectRecoveryKey(Integer userId) throws DaoException;

    /**
     * Deletes record from a user_recovery_key table
     *
     * @param userId id of a user
     * @throws DaoException if {@link SQLException} was caught.
     */
    public abstract void deleteRecoveryKey(Integer userId) throws DaoException;
}
