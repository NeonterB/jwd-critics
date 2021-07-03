package com.epam.jwd_critics.service.impl;

import com.epam.jwd_critics.dao.AbstractCelebrityDao;
import com.epam.jwd_critics.dao.AbstractMovieDao;
import com.epam.jwd_critics.dao.CelebrityDao;
import com.epam.jwd_critics.dao.EntityTransaction;
import com.epam.jwd_critics.dao.MovieDao;
import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.Position;
import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.exception.codes.CelebrityServiceCode;
import com.epam.jwd_critics.service.CelebrityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CelebrityServiceImpl implements CelebrityService {
    private static final Logger logger = LoggerFactory.getLogger(CelebrityServiceImpl.class);
    private final AbstractMovieDao movieDao = new MovieDao();
    private final AbstractCelebrityDao celebrityDao = new CelebrityDao();

    private CelebrityServiceImpl() {

    }

    public static CelebrityServiceImpl getInstance() {
        return CelebrityServiceImplSingleton.INSTANCE;
    }

    @Override
    public List<Celebrity> getAllBetween(int begin, int end) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(celebrityDao);
        List<Celebrity> celebrities;
        try {
            celebrities = celebrityDao.getAllBetween(begin, end);
            for (Celebrity celebrity : celebrities) {
                updateInfo(celebrity);
            }
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return celebrities;
    }

    @Override
    public int getCount() throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(celebrityDao);
        int count;
        try {
            count = celebrityDao.getCount();
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
    public Optional<Celebrity> getEntityById(int id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(celebrityDao);
        Optional<Celebrity> celebrity;
        try {
            celebrity = celebrityDao.getEntityById(id);
            if (celebrity.isPresent()) {
                updateInfo(celebrity.get());
            }
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return celebrity;
    }

    @Override
    public Optional<Celebrity> getEntityByFullName(String firstName, String lastName) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(celebrityDao);
        Optional<Celebrity> celebrity;
        try {
            celebrity = celebrityDao.getEntityByFullName(firstName, lastName);
            if (celebrity.isPresent()) {
                updateInfo(celebrity.get());
            }
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return celebrity;
    }

    @Override
    public void update(Celebrity celebrity) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(celebrityDao);
        try {
            if (!celebrityDao.idExists(celebrity.getId())) {
                throw new ServiceException(CelebrityServiceCode.CELEBRITY_DOES_NOT_EXIST);
            }
            celebrityDao.update(celebrity);
            transaction.commit();
            logger.info("Celebrity with id {} was updated", celebrity.getId());
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    @Override
    public Celebrity create(Celebrity celebrity) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(celebrityDao);
        Celebrity createdCelebrity;
        try {
            if (celebrityDao.celebrityExists(celebrity.getFirstName(), celebrity.getLastName()))
                throw new ServiceException(CelebrityServiceCode.CELEBRITY_EXISTS);
            createdCelebrity = celebrityDao.create(celebrity);
            updateInfo(createdCelebrity);
            transaction.commit();
            logger.info("{} created", createdCelebrity);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return createdCelebrity;
    }

    @Override
    public void delete(int id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(celebrityDao, movieDao);
        try {
            if (celebrityDao.idExists(id)) {
                celebrityDao.delete(id);
            } else {
                throw new ServiceException(CelebrityServiceCode.CELEBRITY_DOES_NOT_EXIST);
            }
            transaction.commit();
            logger.info("Celebrity with id {} was deleted", id);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    private void updateInfo(Celebrity celebrity) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(movieDao);
        try {
            Map<Movie, List<Position>> jobs = movieDao.getJobsByCelebrityId(celebrity.getId());
            celebrity.setJobs(jobs);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    private static class CelebrityServiceImplSingleton {
        private static final CelebrityServiceImpl INSTANCE = new CelebrityServiceImpl();
    }
}
