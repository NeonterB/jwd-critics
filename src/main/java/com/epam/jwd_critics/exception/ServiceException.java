package com.epam.jwd_critics.exception;

import com.epam.jwd_critics.exception.codes.ExceptionCode;

public class ServiceException extends Exception {
    private final ExceptionCode code;

    public ServiceException(ExceptionCode code) {
        this.code = code;
    }

    public ServiceException(DaoException e) {
        super(e);
        this.code = () -> "DAO EXCEPTION: " + e.getMessage();
    }

    public ExceptionCode getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return code.getMessage();
    }
}
