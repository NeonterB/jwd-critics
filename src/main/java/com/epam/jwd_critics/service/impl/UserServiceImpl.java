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
import com.epam.jwd_critics.util.mail.MailType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final AbstractMovieReviewDao reviewDao = new MovieReviewDao();
    private final AbstractUserDao userDao = new UserDao();
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
    public Optional<User> getEntityByEmail(String email) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        Optional<User> user;
        try {
            user = userDao.getEntityByEmail(email);
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
            if (!userDao.idExists(id)) {
                throw new ServiceException(UserServiceCode.USER_DOES_NOT_EXIST);
            }
            userDao.updatePassword(id, passwordAuthenticator.hash(password));
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
        buildAndSendMail(user, locale, key, MailType.ACCOUNT_ACTIVATION);
        logger.info("Activation link was sent to user with id {}", user.getId());
    }

    @Override
    public void buildAndSendRecoveryMail(User user, String key, String locale) {
        buildAndSendMail(user, locale, key, MailType.PASSWORD_RECOVERY);
        logger.info("Password recovery link was sent to user with id {}", user.getId());
    }

    @Override
    public void createActivationKey(int userId, String key) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        try {
            if (!userDao.idExists(userId)) {
                throw new ServiceException(UserServiceCode.USER_DOES_NOT_EXIST);
            }
            if (userDao.selectActivationKey(userId).isPresent()) {
                throw new ServiceException(UserServiceCode.ACTIVATION_KEY_EXISTS);
            }
            userDao.insertActivationKey(userId, key);
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
    public Optional<String> getActivationKey(int userId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        Optional<String> key;
        try {
            if (!userDao.idExists(userId)) {
                throw new ServiceException(UserServiceCode.USER_DOES_NOT_EXIST);
            }
            key = userDao.selectActivationKey(userId);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return key;
    }

    @Override
    public void deleteActivationKey(int userId, String key) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        try {
            if (!userDao.idExists(userId)) {
                throw new ServiceException(UserServiceCode.USER_DOES_NOT_EXIST);
            }
            Optional<String> expected = userDao.selectActivationKey(userId);
            if (!expected.isPresent()) {
                throw new ServiceException(UserServiceCode.USER_DOES_NOT_HAVE_ACTIVATION_KEY);
            } else if (!key.equals(expected.get())) {
                throw new ServiceException(UserServiceCode.WRONG_ACTIVATION_KEY);
            }
            userDao.deleteActivationKey(userId);
            transaction.commit();
            logger.info("Activation key deleted for user with id {}", userId);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    @Override
    public void createRecoveryKey(int userId, String key) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        try {
            if (!userDao.idExists(userId)) {
                throw new ServiceException(UserServiceCode.USER_DOES_NOT_EXIST);
            }
            if (!userDao.selectRecoveryKey(userId).isPresent()) {
                throw new ServiceException(UserServiceCode.RECOVERY_KEY_EXISTS);
            }
            userDao.insertRecoveryKey(userId, key);
            transaction.commit();
            logger.info("Recovery key created for user with id {}", userId);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    @Override
    public Optional<String> getRecoveryKey(int userId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        Optional<String> key;
        try {
            if (!userDao.idExists(userId)) {
                throw new ServiceException(UserServiceCode.USER_DOES_NOT_EXIST);
            }
            key = userDao.selectRecoveryKey(userId);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return key;
    }

    @Override
    public void deleteRecoveryKey(int userId, String key) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        try {
            if (!userDao.idExists(userId)) {
                throw new ServiceException(UserServiceCode.USER_DOES_NOT_EXIST);
            }
            Optional<String> expected = userDao.selectRecoveryKey(userId);
            if (!expected.isPresent()) {
                throw new ServiceException(UserServiceCode.USER_DOES_NOT_HAVE_RECOVERY_KEY);
            } else if (!key.equals(expected.get())) {
                throw new ServiceException(UserServiceCode.WRONG_RECOVERY_KEY);
            }
            userDao.deleteRecoveryKey(userId);
            transaction.commit();
            logger.info("Recovery key deleted for user with id {}", userId);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    private void buildAndSendMail(User user, String locale, String key, MailType mailType) {
        MailBuilder mailBuilder = new MailBuilder(mailType);
        String emailSubject = mailBuilder.buildMailSubject(locale);
        String emailBody = mailBuilder.buildMailBody(user, key, locale);
        MailSender mailSender = new MailSender(emailSubject, emailBody, user.getEmail());
        mailSender.send();
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
