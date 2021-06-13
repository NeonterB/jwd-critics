package com.epam.jwd_critics.exception.codes;

public enum MovieServiceCode implements ExceptionCode {
    MOVIE_DOES_NOT_EXIST("Movie does not exist");
    private final String msg;

    MovieServiceCode(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
