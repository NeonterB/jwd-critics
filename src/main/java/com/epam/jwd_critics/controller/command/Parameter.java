package com.epam.jwd_critics.controller.command;

public enum Parameter {
    COMMAND("command"),
    CURRENT_PAGE("page"),
    NEW_PAGE("newPage"),
    LANG("lang"),
    PICTURE("picture"),
    MOVIE_ID("movieId"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    EMAIL("email"),
    PASSWORD("password"),
    LOGIN("login");
    private final String name;

    Parameter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
