package com.epam.jwd_critics.service;

import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User login(String login, String password) throws ServiceException;

    User register(String firstName, String lastName, String email, String login, char[] password) throws ServiceException;

    List<User> findAll() throws ServiceException;

    Optional<User> findById(Integer id) throws ServiceException;

    User ban(Integer id) throws ServiceException;

    User activate(Integer id) throws ServiceException;

    User toAdmin(Integer id) throws ServiceException;

    User toUser(Integer id) throws ServiceException;

    void delete(Integer id) throws ServiceException;
}
