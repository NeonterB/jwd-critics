package com.epam.jwd_critics.controller.command;

public enum ServletDestination implements Destination {
    MAIN_PAGE("/pages/common/main.jsp"),
    SIGN_IN_PAGE("/pages/guest/signIn.jsp"),
    ERROR_404("/pages/error/404.jsp");
    private final String path;

    ServletDestination(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }
}
