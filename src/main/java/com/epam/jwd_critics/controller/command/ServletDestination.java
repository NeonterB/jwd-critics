package com.epam.jwd_critics.controller.command;

public enum ServletDestination implements Destination {
    MAIN_PAGE("/pages/main.jsp"),
    LOGIN_PAGE("/pages/login.jsp");
    private final String path;

    ServletDestination(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }
}
