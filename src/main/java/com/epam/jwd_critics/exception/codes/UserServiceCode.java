package com.epam.jwd_critics.exception.codes;

public enum UserServiceCode implements ExceptionCode {
    USER_DOES_NOT_EXIST("User does not exist"),
    USER_DOES_NOT_HAVE_ACTIVATION_KEY("User does not have activation key"),
    ACTIVATION_KEY_EXISTS("Activation key already exists"),
    WRONG_ACTIVATION_KEY("Wrong activation key"),
    RECOVERY_KEY_EXISTS("Recover key already exists"),
    USER_DOES_NOT_HAVE_RECOVERY_KEY("User does not have recovery key"),
    WRONG_RECOVERY_KEY("Wrong recover key"),
    LOGIN_EXISTS("This login is unavailable"),
    EMAIL_EXISTS("Account with this email already exists"),
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
