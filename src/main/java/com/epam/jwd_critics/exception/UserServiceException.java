package com.epam.jwd_critics.exception;

import com.epam.jwd_critics.exception.codes.UserServiceCode;

public class UserServiceException extends Exception{
    UserServiceCode code;

    public UserServiceException(UserServiceCode code) {
        this.code = code;
    }

    public UserServiceCode getCode() {
        return code;
    }
}
