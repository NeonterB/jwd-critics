package com.epam.jwd_critics.service.impl;

import com.epam.jwd_critics.dao.AbstractMovieReviewDao;
import com.epam.jwd_critics.dao.AbstractUserDao;
import com.epam.jwd_critics.dao.EntityTransaction;
import com.epam.jwd_critics.dao.MovieReviewDao;
import com.epam.jwd_critics.dao.UserDao;
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.entity.Role;
import com.epam.jwd_critics.entity.Status;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.exception.UserServiceException;
import com.epam.jwd_critics.exception.codes.UserServiceCode;
import com.epam.jwd_critics.service.UserService;
import com.epam.jwd_critics.util.PasswordAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final AbstractMovieReviewDao reviewDao = MovieReviewDao.getInstance();
    private final AbstractUserDao userDao = UserDao.getInstance();
    private final PasswordAuthenticator passwordAuthenticator = new PasswordAuthenticator();

    private UserServiceImpl() {

    }

    public static UserServiceImpl getInstance() {
        return UserServiceImpl.UserServiceImplSingleton.INSTANCE;
    }

    @Override
    public User login(String login, String password) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(reviewDao, userDao);
        User user;
        try {
            user = userDao.getEntityByLogin(login).orElseThrow(() -> new UserServiceException(UserServiceCode.USER_DOES_NOT_EXIST));
            if (!passwordAuthenticator.authenticate(password, user.getPassword())) {
                throw new UserServiceException(UserServiceCode.INCORRECT_PASSWORD);
            } else if (user.getStatus().equals(Status.BANNED)) {
                throw new UserServiceException(UserServiceCode.USER_IS_BANNED);
            } else if (user.getStatus().equals(Status.INACTIVE)) {
                throw new UserServiceException(UserServiceCode.USER_IS_INACTIVE);
            }
            updateInfo(user);
            transaction.commit();
            logger.info("{} logged in", user);

        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } catch (UserServiceException e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
        return user;
    }

    @Override
    public User register(String firstName, String lastName, String email, String login, char[] password) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        User userToRegister = User.newBuilder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setLogin(login)
                .build();
        try {
            if (userDao.loginExists(login)) {
                throw new UserServiceException(UserServiceCode.LOGIN_EXISTS);
            } else {
                userToRegister.setPassword(passwordAuthenticator.hash(password));
                setDefaultFields(userToRegister);
                userToRegister = userDao.create(userToRegister);
            }
            transaction.commit();
            logger.info("{} registered", userToRegister);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } catch (UserServiceException e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
        return userToRegister;
    }

    @Override
    public List<User> getAll() throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        List<User> userList;
        try {
            userList = userDao.getAll();
            for (User user : userList) {
                updateInfo(user);
            }
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return userList;
    }

    @Override
    public Optional<User> getEntityById(Integer id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        Optional<User> user;
        try {
            user = userDao.getEntityById(id);
            if (user.isPresent())
                updateInfo(user.get());
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return user;
    }

    @Override
    public User ban(Integer id) throws ServiceException {
        return updateStatus(id, Status.BANNED);
    }

    @Override
    public User activate(Integer id) throws ServiceException {
        return updateStatus(id, Status.ACTIVE);
    }

    @Override
    public User toAdmin(Integer id) throws ServiceException {
        return updateRole(id, Role.ADMIN);
    }

    @Override
    public User toUser(Integer id) throws ServiceException {
        return updateRole(id, Role.USER);
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao, reviewDao);
        try {
            User userToDelete = userDao.getEntityById(id).orElseThrow(() -> new UserServiceException(UserServiceCode.USER_DOES_NOT_EXIST));
            userDao.delete(id);
            transaction.commit();
            logger.info("{} was deleted", userToDelete);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } catch (UserServiceException e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
    }

    private User updateStatus(Integer id, Status status) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        User user;
        try {
            user = userDao.getEntityById(id).orElseThrow(() -> new UserServiceException(UserServiceCode.USER_DOES_NOT_EXIST));
            user.setStatus(status);
            userDao.update(user);
            transaction.commit();
            logger.info("Status of user with id {} was changed to {}", id, status);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } catch (UserServiceException e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
        return user;
    }

    private User updateRole(Integer id, Role role) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        User user;
        try {
            user = userDao.getEntityById(id).orElseThrow(() -> new UserServiceException(UserServiceCode.USER_DOES_NOT_EXIST));
            user.setRole(role);
            userDao.update(user);
            transaction.commit();
            logger.info("Role of user with id {} was changed to {}", id, role);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } catch (UserServiceException e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
        return user;
    }

    private void updateInfo(User user) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(reviewDao);
        try {
            List<MovieReview> reviews = reviewDao.getMovieReviewsByUserId(user.getId());
            user.setReviews(reviews);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    private void setDefaultFields(User user) {
        user.setRating(0);
        user.setRole(Role.USER);
        user.setStatus(Status.INACTIVE);
    }

    private static class UserServiceImplSingleton {
        private static final UserServiceImpl INSTANCE = new UserServiceImpl();
    }
}
