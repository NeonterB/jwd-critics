package com.epam.jwd_critics.exception.codes;

public enum UserServiceCode implements ExceptionCode {
    USER_DOES_NOT_EXIST("User does not exist"),
    ACTIVATION_KEY_EXISTS("Activation key already exists"),
    WRONG_ACTIVATION_KEY("Wrong activation key"),
    LOGIN_EXISTS("Login already exists"),
    INCORRECT_PASSWORD("Incorrect password"),
    USER_IS_BANNED("User is banned"),
    USER_IS_INACTIVE("User is inactive"),
    CAN_NOT_DELETE_ADMIN("Use is admin");
    private final String msg;

    UserServiceCode(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
