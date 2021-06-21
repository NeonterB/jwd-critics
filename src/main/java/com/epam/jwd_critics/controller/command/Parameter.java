package com.epam.jwd_critics.controller.command;

public enum Parameter {
    COMMAND("command"),
    CURRENT_PAGE("page"),
    NEW_MOVIE_PAGE("newMoviePage"),
    NEW_REVIEW_PAGE("newReviewPage"),
    LANG("lang"),
    PICTURE("picture"),
    MOVIE_ID("movieId"),
    USER_ID("userId"),
    REVIEW_ID("reviewId"),
    MOVIE_REVIEW_TEXT("movieReviewText"),
    MOVIE_REVIEW_SCORE("movieReviewScore"),
    MOVIE_REVIEW_ID("movieReviewId"),
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
