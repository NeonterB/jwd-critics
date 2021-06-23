package com.epam.jwd_critics.service;

import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User login(String login, String password) throws ServiceException;

    User register(String firstName, String lastName, String email, String login, char[] password) throws ServiceException;

    List<User> getAllBetween(int begin, int end) throws ServiceException;

    int getCount() throws ServiceException;

    Optional<User> getEntityById(int id) throws ServiceException;

    void update(User user) throws ServiceException;

    void updatePassword(int id, char[] password) throws ServiceException;

    void delete(int id) throws ServiceException;
}
