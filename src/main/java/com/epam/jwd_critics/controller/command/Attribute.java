package com.epam.jwd_critics.controller.command;

public enum Attribute {
    USER_ID("userId"),
    USER_ROLE("userRole"),
    VALIDATION_ERRORS("validationErrors"),
    SERVICE_ERROR("serviceError");
    private final String name;

    Attribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
