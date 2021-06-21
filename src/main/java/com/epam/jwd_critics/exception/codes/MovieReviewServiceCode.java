package com.epam.jwd_critics.exception.codes;

public enum MovieReviewServiceCode implements ExceptionCode {
    REVIEW_DOES_NOT_EXIST("Movie review does not exist"),
    USER_ALREADY_LEFT_A_REVIEW("User already left a review");
    private final String msg;

    MovieReviewServiceCode(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
