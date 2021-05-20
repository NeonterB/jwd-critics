package com.epam.jwd_critics.exception;

import com.epam.jwd_critics.exception.codes.MovieReviewServiceCode;

public class MovieReviewServiceException extends ServiceException {
    MovieReviewServiceCode code;

    public MovieReviewServiceException(MovieReviewServiceCode code) {
        this.code = code;
    }

    public MovieReviewServiceCode getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return "MovieReviewServiceException occurred! exception code - " + code;
    }
}
