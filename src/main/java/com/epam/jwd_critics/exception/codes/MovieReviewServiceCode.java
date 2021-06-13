package com.epam.jwd_critics.exception.codes;

public enum MovieReviewServiceCode implements ExceptionCode {
    REVIEW_DOES_NOT_EXIST("Movie review does not exist");
    private final String msg;

    MovieReviewServiceCode(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
