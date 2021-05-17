package com.epam.jwd_critics.service;

import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.exception.UserServiceException;
import com.epam.jwd_critics.exception.codes.UserServiceCode;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User login(String login, String password) throws ServiceException;

    User register(String firstName, String lastName, String email, String login, String password) throws ServiceException;

    List<User> findAll();

    Optional<User> findById(Integer id);

    UserServiceCode activate(Integer id) throws ServiceException;

    UserServiceCode block(Integer id) throws ServiceException;

    UserServiceCode delete(Integer id) throws ServiceException;
}
