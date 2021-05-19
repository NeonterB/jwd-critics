package com.epam.jwd_critics.exception;

import com.epam.jwd_critics.exception.codes.CelebrityServiceCode;

public class CelebrityServiceException extends ServiceException {
    CelebrityServiceCode code;

    public CelebrityServiceException(CelebrityServiceCode code) {
        this.code = code;
    }

    public CelebrityServiceCode getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return "CelebrityServiceException occurred! exception code - " + code;
    }
}
