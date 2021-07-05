package com.epam.jwd_critics.service;

import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    /**
     * Finds user with given login and password.
     *
     * @param login    login of a user.
     * @param password password of a user.
     * @return user.
     * @throws ServiceException if user with given login does not exist, if user with given password does not exist,
     *                          if user is banned, or if {@link DaoException} was caught.
     */
    User login(String login, String password) throws ServiceException;

    /**
     * Creates new user with given parameters.
     *
     * @param firstName first name of a user.
     * @param lastName  last name of a user.
     * @param email     email of a user.
     * @param login     login of a user.
     * @param password  password of a user.
     * @return registered user.
     * @throws ServiceException if user with given login already exists, if user with given email already exists,
     *                          or if {@link DaoException} was caught.
     */
    User register(String firstName, String lastName, String email, String login, char[] password) throws ServiceException;

    /**
     * Gets all users with limit.
     *
     * @param begin from.
     * @param end   to.
     * @return {@link List} of users.
     * @throws ServiceException if {@link DaoException} was caught.
     */
    List<User> getAllBetween(int begin, int end) throws ServiceException;

    /**
     * Gets count of users.
     *
     * @return count of users.
     * @throws ServiceException if {@link DaoException} was caught.
     */
    int getCount() throws ServiceException;

    /**
     * Finds user with given id.
     *
     * @param id user id.
     * @return {@link Optional} of user.
     * @throws ServiceException if {@link DaoException} was caught.
     */
    Optional<User> getEntityById(int id) throws ServiceException;

    /**
     * Finds user with given email.
     *
     * @param email email of a user.
     * @return {@link Optional} of user.
     * @throws ServiceException if {@link DaoException} was caught.
     */
    Optional<User> getEntityByEmail(String email) throws ServiceException;

    /**
     * Finds and updates user.
     *
     * @param user user to update.
     * @throws ServiceException if given user does not exist or {@link DaoException} was caught.
     */
    void update(User user) throws ServiceException;

    /**
     * Updates password of user with given id.
     *
     * @param id       id of a user record
     * @param password new password value
     * @throws ServiceException if given user does not exist or {@link DaoException} was caught.
     */
    void updatePassword(int id, char[] password) throws ServiceException;

    /**
     * Deletes user with given id.
     *
     * @param id id of a user.
     * @throws ServiceException if given user does not exist or {@link DaoException} was caught.
     */
    void delete(int id) throws ServiceException;

    /**
     * Builds and sends an account activation mail to user email address
     *
     * @param user   recipient
     * @param key    activation key
     * @param locale language
     */
    void buildAndSendActivationMail(User user, String key, String locale);

    /**
     * Builds and sends a password recovery mail to user email address
     *
     * @param user   recipient
     * @param key    recovery key
     * @param locale language
     */
    void buildAndSendRecoveryMail(User user, String key, String locale);

    /**
     * Creates new activation key for user with given id
     *
     * @param userId id of a user
     * @param key    activation key
     * @throws ServiceException if given user does not exist, or if user already has activation key, or if {@link DaoException} was caught.
     */
    void createActivationKey(int userId, String key) throws ServiceException;

    /**
     * Finds an activation key for user with given id.
     *
     * @param userId id of a user.
     * @return {@link Optional} of activation key
     * @throws ServiceException if given user does not exist or {@link DaoException} was caught.
     */
    Optional<String> getActivationKey(int userId) throws ServiceException;

    /**
     * Deletes activation key for user with given id.
     *
     * @param userId id of a user
     * @param key    activation key
     * @throws ServiceException if given user does not exist, or if user does not have an activation key,
     *                          or if activation key id wrong, or if {@link DaoException} was caught.
     */
    void deleteActivationKey(int userId, String key) throws ServiceException;

    /**
     * Creates new password recovery key for user with given id
     *
     * @param userId id of a user
     * @param key    recovery key
     * @throws ServiceException if given user does not exist, or if user already has recovery key, or if {@link DaoException} was caught.
     */
    void createRecoveryKey(int userId, String key) throws ServiceException;

    /**
     * Finds a recovery key for user with given id.
     *
     * @param userId id of a user.
     * @return {@link Optional} of recovery key
     * @throws ServiceException if given user does not exist or {@link DaoException} was caught.
     */
    Optional<String> getRecoveryKey(int userId) throws ServiceException;

    /**
     * Deletes recovery key for user with given id.
     *
     * @param userId id of a user
     * @param key    recovery key
     * @throws ServiceException if given user does not exist, or if user does not have an recovery key,
     *                          or if recovery key id wrong, or if {@link DaoException} was caught.
     */
    void deleteRecoveryKey(int userId, String key) throws ServiceException;
}
