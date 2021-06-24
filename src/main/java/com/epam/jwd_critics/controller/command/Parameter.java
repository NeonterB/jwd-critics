package com.epam.jwd_critics.controller.command;

public enum Parameter {
    COMMAND("command"),
    LANG("lang"),
    CURRENT_PAGE("currentPage"),
    CURRENT_PICTURE("currentPicture"),

    NEW_MOVIES_PAGE("newMoviesPage"),
    NEW_REVIEWS_PAGE("newReviewsPage"),
    NEW_USERS_PAGE("newUsersPage"),

    MOVIE_ID("movieId"),
    USER_ID("userId"),
    REVIEW_ID("reviewId"),

    MOVIE_REVIEW_TEXT("movieReviewText"),
    MOVIE_REVIEW_SCORE("movieReviewScore"),
    MOVIE_REVIEW_ID("movieReviewId"),

    IMAGE_PATH("imagePath"),

    NEW_STATUS("newStatus"),

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
