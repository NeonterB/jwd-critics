package com.epam.jwd_critics.controller.command;

public enum ServletDestination implements Destination {
    MAIN_PAGE("/pages/main.jsp"),
    SIGN_IN_PAGE("/pages/signIn.jsp");
    private final String path;

    ServletDestination(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }
}
