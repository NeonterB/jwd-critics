package com.epam.jwd_critics.exception;

import com.epam.jwd_critics.exception.codes.MovieServiceCode;

public class MovieServiceException extends ServiceException {
    MovieServiceCode code;

    public MovieServiceException(MovieServiceCode code) {
        this.code = code;
    }

    public MovieServiceCode getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return "MovieServiceException occurred! exception code - " + code;
    }
}
