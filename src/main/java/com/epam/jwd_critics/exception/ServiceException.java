package com.epam.jwd_critics.exception;

import com.epam.jwd_critics.exception.codes.ExceptionCode;

import java.net.ServerSocket;

public class ServiceException extends Exception {
    private final ExceptionCode code;

    public ServiceException(ExceptionCode code) {
        this.code = code;
    }

    public ExceptionCode getCode() {
        return code;
    }

    public ServiceException(Throwable throwable){
        super(throwable);
        this.code = () -> "DAO EXCEPTION: " + throwable.getMessage();
    }

    @Override
    public String getMessage() {
        return code.getMessage();
    }
}
