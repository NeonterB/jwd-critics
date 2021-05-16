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
import com.epam.jwd_critics.service.UserService;
import com.epam.jwd_critics.service.responses.UserServiceCode;
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

    @Override
    public UserServiceCode login(String login, String password) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(reviewDao, userDao);
        try {
            if (!userDao.loginExists(login)) {
                return UserServiceCode.LOGIN_DOES_NOT_EXIST;
            } else {
                SecureRandom random = new SecureRandom();
                byte[] salt = new byte[16];
                random.nextBytes(salt);
                KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                byte[] hash = factory.generateSecret(spec).getEncoded();
                String encryptedPassword = Arrays.toString(hash);

                User user = userDao.findEntityByLogin(login).get();
                if (!encryptedPassword.equals(user.getPassword())) {
                    return UserServiceCode.INCORRECT_PASSWORD;
                } else {
                    if (user.getStatus().equals(Status.BANNED)) {
                        return UserServiceCode.USER_IS_BLOCKED;
                    } else {
                        List<MovieReview> reviews = reviewDao.getReviewsByUserId(user.getId());
                        user.setReviews(reviews);
                    }
                }
            }
            transaction.commit();
        } catch (DaoException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return UserServiceCode.SUCCESS;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public UserServiceCode register(String login, String email, String firstName, String secondName, String password) {
        return null;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public UserServiceCode activate(Integer id) throws ServiceException {
        return null;
    }

    @Override
    public UserServiceCode block(Integer id) throws ServiceException {
        return null;
    }

    @Override
    public UserServiceCode delete(Integer id) throws ServiceException {
        return null;
    }
}
