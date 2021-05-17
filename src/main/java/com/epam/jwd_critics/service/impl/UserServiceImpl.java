package com.epam.jwd_critics.service.impl;

import com.epam.jwd_critics.dao.AbstractMovieReviewDao;
import com.epam.jwd_critics.dao.AbstractUserDao;
import com.epam.jwd_critics.dao.EntityTransaction;
import com.epam.jwd_critics.dao.MovieReviewDao;
import com.epam.jwd_critics.dao.UserDao;
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.entity.Status;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.exception.UserServiceException;
import com.epam.jwd_critics.service.UserService;
import com.epam.jwd_critics.exception.codes.UserServiceCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
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
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Arrays.toString(hash);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User login(String login, String password) throws UserServiceException, ServiceException {
        EntityTransaction transaction = new EntityTransaction(reviewDao, userDao);
        User user = null;
        try {
            if (!userDao.loginExists(login)) {
                throw new UserServiceException(UserServiceCode.LOGIN_DOES_NOT_EXIST);
            } else {
                String encryptedPassword = encryptPassword(password);
                user = userDao.findEntityByLogin(login).get();
                if (!encryptedPassword.equals(user.getPassword())) {
                    throw new UserServiceException(UserServiceCode.INCORRECT_PASSWORD);
                } else {
                    if (user.getStatus().equals(Status.BANNED)) {
                        throw new UserServiceException(UserServiceCode.USER_IS_BANNED);
                    } else {
                        List<MovieReview> reviews = reviewDao.getReviewsByUserId(user.getId());
                        user.setReviews(reviews);
                    }
                }
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
        EntityTransaction transaction = null;
        User userToRegister = User.newBuilder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setLogin(login)
                .build();
        try {
            transaction = new EntityTransaction(userDao);
            if (userDao.loginExists(login)) {
                throw new UserServiceException(UserServiceCode.LOGIN_EXISTS);
            } else {
                String encryptedPassword = encryptPassword(password);
                userToRegister.setPassword(encryptedPassword);
                userToRegister = userDao.create(userToRegister);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return userToRegister;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public UserServiceCode activate(Integer id) throws UserServiceException {
        return null;
    }

    @Override
    public UserServiceCode block(Integer id) throws UserServiceException {
        return null;
    }

    @Override
    public UserServiceCode delete(Integer id) throws UserServiceException {
        return null;
    }
}
