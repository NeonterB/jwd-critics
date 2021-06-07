package com.epam.jwd_critics.model.service.impl;

import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.exception.UserServiceException;
import com.epam.jwd_critics.exception.codes.UserServiceCode;
import com.epam.jwd_critics.model.entity.Status;
import com.epam.jwd_critics.model.entity.User;
import com.epam.jwd_critics.model.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class UserServiceImplTest {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImplTest.class);
    private static final UserService userService = UserServiceImpl.getInstance();
    private static User user;

//    public static void main(String[] args) throws ServiceException {
//
//        userService.updatePassword(1, "75315962mM*".toCharArray());
//        userService.updatePassword(2, "75315962sS*".toCharArray());
//        userService.updatePassword(3, "75315962dD*".toCharArray());
//    }

    @BeforeAll
    public static void register() {
        Assertions.assertDoesNotThrow(() -> {
            user = userService.register("Test", "Testovich", "test@email.com", "testLogin", "753159".toCharArray());
            user.setStatus(Status.ACTIVE);
            userService.update(user);
        });
        try {
            user = userService.register("Test", "Testovich", "test@email.com", "testLogin", "753159".toCharArray());
            user.setStatus(Status.ACTIVE);
            userService.update(user);
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            Assertions.assertEquals(((UserServiceException) e).getCode(), UserServiceCode.LOGIN_EXISTS);
        }
    }

    @Test
    public void login() {
        Assertions.assertDoesNotThrow(() -> {
            user = userService.login("testLogin", "753159");
        });
        try {
            user = userService.login("fakeTestLogin", "753159");
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            Assertions.assertEquals(((UserServiceException) e).getCode(), UserServiceCode.USER_DOES_NOT_EXIST);
        }
        try {
            user = userService.login("testLogin", "111111");
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            Assertions.assertEquals(((UserServiceException) e).getCode(), UserServiceCode.INCORRECT_PASSWORD);
        }
    }

    @AfterAll
    public static void clear() {
        try {
            userService.delete(user.getId());
        } catch (ServiceException e) {
            logger.debug(e.getMessage(), e);
        }
    }
}