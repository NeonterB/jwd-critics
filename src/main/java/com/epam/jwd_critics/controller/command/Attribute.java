package com.epam.jwd_critics.controller.command;

public enum Attribute {
    USER_ID("userId"),
    USER_ROLE("userRole"),
    LANG("lang"),
    ALL_MOVIES_CURRENT_PAGE("allMoviesCurrentPage"),
    REVIEWS_CURRENT_PAGE("reviewsCurrentPage"),
    NEW_PAGE("newPage"),
    MOVIES_TO_DISPLAY("allMoviesList"),
    MOVIE_COUNT("movieCount"),
    MOVIE("movie"),
    MOVIE_ID("movieId"),
    REVIEWS_TO_DISPLAY("reviewsToDisplay"),
    REVIEW_COUNT("reviewCount"),
    VALIDATION_ERRORS("validationErrors"),
    SERVICE_ERROR("serviceError"),
    GLOBAL_ERROR("globalError"),
    REPORT_MESSAGE("reportMessage");
    private final String name;

    Attribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
