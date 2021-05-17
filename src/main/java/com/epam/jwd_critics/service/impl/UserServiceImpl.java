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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final AbstractMovieReviewDao reviewDao = MovieReviewDao.getInstance();
    private final AbstractUserDao userDao = UserDao.getInstance();

    private UserServiceImpl() {

    }

    public static UserServiceImpl getInstance() {
        return UserServiceImpl.UserServiceImplSingleton.INSTANCE;
    }

    private static class UserServiceImplSingleton {
        private static final UserServiceImpl INSTANCE = new UserServiceImpl();
    }

    private String encryptPassword(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        byte[] hash = new byte[0];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(hash);
    }

    @Override
    public User login(String login, String password) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(reviewDao, userDao);
        User user;
        try {
            String encryptedPassword = encryptPassword(password);
            user = userDao.findEntityByLogin(login).orElseThrow(() -> new UserServiceException(UserServiceCode.USER_DOES_NOT_EXIST));
            if (!encryptedPassword.equals(user.getPassword())) {
                throw new UserServiceException(UserServiceCode.INCORRECT_PASSWORD);
            } else if (user.getStatus().equals(Status.BANNED)) {
                throw new UserServiceException(UserServiceCode.USER_IS_BANNED);
            } else if (user.getStatus().equals(Status.INACTIVE)) {
                throw new UserServiceException(UserServiceCode.USER_IS_INACTIVE);
            }
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
    public User register(String firstName, String lastName, String email, String login, String password) throws ServiceException {
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
                String encryptedPassword = encryptPassword(password);
                userToRegister.setPassword(encryptedPassword);
                userToRegister = userDao.create(userToRegister);
            }
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
    public List<User> findAll() throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        List<User> userList;
        try {
            userList = userDao.findAll();
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
    public Optional<User> findById(Integer id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        Optional<User> user;
        try {
            user = userDao.findEntityById(id);
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
    public void delete(Integer id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao, reviewDao);
        try {
            User userToDelete = userDao.findEntityById(id).orElseThrow(() -> new UserServiceException(UserServiceCode.USER_DOES_NOT_EXIST));
            if (!userToDelete.getRole().equals(Role.ADMIN)) {
                List<MovieReview> reviews = reviewDao.getReviewsByUserId(id);
                for (MovieReview review : reviews) {
                    reviewDao.deleteEntityById(review.getId());
                }
                userDao.deleteEntityById(id);
            } else {
                throw new UserServiceException(UserServiceCode.CAN_NOT_DELETE_ADMIN);
            }

            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    private User updateStatus(Integer id, Status status) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        User user;
        try {
            user = userDao.findEntityById(id).orElseThrow(() -> new UserServiceException(UserServiceCode.USER_DOES_NOT_EXIST));
            user.setStatus(status);
            userDao.update(user);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return user;
    }
}
