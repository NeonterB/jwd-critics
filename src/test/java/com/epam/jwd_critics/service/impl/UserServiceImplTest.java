package com.epam.jwd_critics.service.impl;

import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.exception.UserServiceException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class UserServiceImplTest {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImplTest.class);
    private static final UserServiceImpl userService = UserServiceImpl.getInstance();
    private static User user;

    @Test
    public void register() {
        Assertions.assertDoesNotThrow(() -> {
            user = userService.register("Test", "Testovich", "test@email.com", "testLogin", "753159");
        });
        logger.info(user.toString());
        Assertions.assertThrows(UserServiceException.class, () -> user = userService.register("Test", "Testovich", "test@email.com", "testLogin", "753159"));
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