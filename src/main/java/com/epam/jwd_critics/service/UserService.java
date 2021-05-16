package com.epam.jwd_critics.service;

import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.service.responses.UserServiceCode;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserServiceCode login(String login, String password) throws ServiceException;
    List<User> findAll();
    UserServiceCode register(String login, String email, String firstName, String secondName, String password);
    Optional<User> findById(Integer id);
    UserServiceCode activate(Integer id) throws ServiceException;
    UserServiceCode block(Integer id) throws ServiceException;
    UserServiceCode delete(Integer id) throws ServiceException;
}
