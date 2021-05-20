package com.epam.jwd_critics.service.impl;

import com.epam.jwd_critics.dao.AbstractCelebrityDao;
import com.epam.jwd_critics.dao.AbstractMovieDao;
import com.epam.jwd_critics.dao.AbstractMovieReviewDao;
import com.epam.jwd_critics.dao.CelebrityDao;
import com.epam.jwd_critics.dao.EntityTransaction;
import com.epam.jwd_critics.dao.MovieDao;
import com.epam.jwd_critics.dao.MovieReviewDao;
import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.Position;
import com.epam.jwd_critics.exception.CelebrityServiceException;
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
    private final AbstractMovieReviewDao reviewDao = MovieReviewDao.getInstance();
    private final AbstractMovieDao movieDao = MovieDao.getInstance();
    private final AbstractCelebrityDao celebrityDao = CelebrityDao.getInstance();

    private CelebrityServiceImpl() {

    }

    public static CelebrityServiceImpl getInstance() {
        return CelebrityServiceImpl.CelebrityServiceImplSingleton.INSTANCE;
    }

    @Override
    public List<Celebrity> getAll() throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(celebrityDao);
        List<Celebrity> celebrities;
        try {
            celebrities = celebrityDao.getAll();
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
    public Optional<Celebrity> getEntityById(Integer id) throws ServiceException {
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
    public void update(Celebrity celebrity) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(celebrityDao);
        try {
            if (!celebrityDao.idExists(celebrity.getId())) {
                throw new CelebrityServiceException(CelebrityServiceCode.CELEBRITY_DOES_NOT_EXIST);
            }
            celebrityDao.update(celebrity);
            transaction.commit();
            logger.info("Celebrity with id {} was updated", celebrity.getId());
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } catch (CelebrityServiceException e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
    }

    @Override
    public Celebrity create(Celebrity celebrity) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(celebrityDao);
        Celebrity createdCelebrity;
        try {
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
    public void delete(Integer id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(celebrityDao, movieDao);
        try {
            Celebrity celebrityToDelete = celebrityDao.getEntityById(id)
                    .orElseThrow(() -> new CelebrityServiceException(CelebrityServiceCode.CELEBRITY_DOES_NOT_EXIST));
            updateInfo(celebrityToDelete);
            for (Movie movie : celebrityToDelete.getJobs().keySet()) {
                movie.getStaff().remove(celebrityToDelete);
                movieDao.update(movie);
            }
            celebrityDao.delete(id);
            transaction.commit();
            logger.info("{} was deleted", celebrityToDelete);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } catch (CelebrityServiceException e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
    }

    private void updateInfo(Celebrity celebrity) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(celebrityDao, movieDao);
        try {
            Map<Movie, List<Position>> jobs = movieDao.getMoviesByCelebrityId(celebrity.getId());
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
