package com.epam.jwd_critics.controller.command;

public enum Attribute {
    USER_ID("userId"),
    USER_ROLE("userRole"),
    LANG("lang"),
    VALIDATION_ERRORS("validationErrors"),
    SERVICE_ERROR("serviceError"),
    GLOBAL_ERROR("globalError");
    private final String name;

    Attribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
