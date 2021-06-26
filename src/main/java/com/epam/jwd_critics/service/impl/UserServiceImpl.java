package com.epam.jwd_critics.service.impl;

import com.epam.jwd_critics.dao.AbstractMovieReviewDao;
import com.epam.jwd_critics.dao.AbstractUserDao;
import com.epam.jwd_critics.dao.EntityTransaction;
import com.epam.jwd_critics.dao.MovieReviewDao;
import com.epam.jwd_critics.dao.UserDao;
import com.epam.jwd_critics.entity.Role;
import com.epam.jwd_critics.entity.Status;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.exception.codes.UserServiceCode;
import com.epam.jwd_critics.service.UserService;
import com.epam.jwd_critics.util.PasswordAuthenticator;
import com.epam.jwd_critics.util.mail.MailBuilder;
import com.epam.jwd_critics.util.mail.MailSender;
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
        return UserServiceImplSingleton.INSTANCE;
    }

    @Override
    public User login(String login, String password) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(reviewDao, userDao);
        User user;
        try {
            user = userDao.getEntityByLogin(login).orElseThrow(() -> new ServiceException(UserServiceCode.USER_DOES_NOT_EXIST));
            if (!passwordAuthenticator.authenticate(password, user.getPassword())) {
                throw new ServiceException(UserServiceCode.INCORRECT_PASSWORD);
            } else if (user.getStatus().equals(Status.BANNED)) {
                throw new ServiceException(UserServiceCode.USER_IS_BANNED);
            }
            logger.info("{}, id = {} logged in", user.getFirstName() + " " + user.getLastName(), user.getId());
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
                throw new ServiceException(UserServiceCode.LOGIN_EXISTS);
            }
            if (userDao.emailExists(email)) {
                throw new ServiceException(UserServiceCode.EMAIL_EXISTS);
            }
            userToRegister.setPassword(passwordAuthenticator.hash(password));
            setDefaultFields(userToRegister);
            userToRegister = userDao.create(userToRegister);
            logger.info("{}, id = {} registered in", userToRegister.getFirstName() + " " + userToRegister.getLastName(), userToRegister.getId());
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return userToRegister;
    }

    @Override
    public List<User> getAllBetween(int begin, int end) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        List<User> userList;
        try {
            userList = userDao.getAllBetween(begin, end);
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
    public int getCount() throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        int count;
        try {
            count = userDao.getCount();
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return count;
    }

    @Override
    public Optional<User> getEntityById(int id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        Optional<User> user;
        try {
            user = userDao.getEntityById(id);
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
    public void update(User user) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        try {
            if (!userDao.idExists(user.getId())) {
                throw new ServiceException(UserServiceCode.USER_DOES_NOT_EXIST);
            }
            userDao.update(user);
            transaction.commit();
            logger.info("User with id {} was updated", user.getId());
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    public void updatePassword(int id, char[] password) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        try {
            if (!userDao.updatePassword(id, passwordAuthenticator.hash(password))) {
                throw new ServiceException(UserServiceCode.USER_DOES_NOT_EXIST);
            }
            transaction.commit();
            logger.info("User with id {} updated password", id);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    @Override
    public void delete(int id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao, reviewDao);
        try {
            if (!userDao.idExists(id)) {
                throw new ServiceException(UserServiceCode.USER_DOES_NOT_EXIST);
            }
            userDao.delete(id);
            transaction.commit();
            logger.info("User with id {} was deleted", id);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } catch (ServiceException e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
    }

    @Override
    public void buildAndSendActivationMail(User user, String key, String locale) {
        String emailSubject = MailBuilder.buildEmailSubject(locale);
        String emailBody = MailBuilder.buildEmailBody(user, key, locale);
        MailSender mailSender = new MailSender(emailSubject, emailBody, user.getEmail());
        mailSender.send();
        logger.info("Activation link was sent to user with id {}", user.getId());
    }

    @Override
    public void createActivationKey(int userId, String key) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        try {
            if (!userDao.insertActivationKey(userId, key)) {
                throw new ServiceException(UserServiceCode.ACTIVATION_KEY_EXISTS);
            }
            transaction.commit();
            logger.info("Activation key created for user with id {}", userId);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    @Override
    public void deleteActivationKey(int userId, String key) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        try {
            if (!userDao.deleteActivationKey(userId, key)) {
                throw new ServiceException(UserServiceCode.WRONG_ACTIVATION_KEY);
            }
            transaction.commit();
            logger.info("Activation key deleted for user with id {}", userId);
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
