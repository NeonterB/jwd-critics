package com.epam.jwd_critics.controller.command;

public enum Attribute {
    USER_ID("userId"),
    USER_ROLE("userRole");
    private final String name;

    Attribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
