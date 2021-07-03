package com.epam.jwd_critics.exception.codes;

public enum CelebrityServiceCode implements ExceptionCode {
    CELEBRITY_DOES_NOT_EXIST("Celebrity does not exist"),
    CELEBRITY_EXISTS("Celebrity already exists");
    private final String msg;

    CelebrityServiceCode(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
