package com.epam.jwd_critics.controller.command;

public enum ServletDestination implements Destination {
    MAIN("/pages/common/main.jsp"),
    SIGN_IN("/pages/guest/sign_in.jsp"),
    ALL_MOVIES("/pages/common/all_movies.jsp"),
    MOVIE("/pages/common/movie.jsp"),
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
