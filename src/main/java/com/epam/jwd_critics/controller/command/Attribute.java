package com.epam.jwd_critics.controller.command;

public enum Attribute {
    LOGIN("login"),
    EMAIL("email"),
    PASSWORD("password");
    private final String parameterName;

    Attribute(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }
}
