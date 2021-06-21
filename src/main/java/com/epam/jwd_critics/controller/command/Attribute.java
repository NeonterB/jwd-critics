package com.epam.jwd_critics.controller.command;

public enum Attribute {
    USER("user"),
    LANG("lang"),
    ALL_MOVIES_CURRENT_PAGE("allMoviesCurrentPage"),
    REVIEWS_CURRENT_PAGE("reviewsCurrentPage"),
    NEW_PAGE("newPage"),
    CURRENT_PAGE("currentPage"),
    MOVIES_TO_DISPLAY("allMoviesList"),
    MOVIE_COUNT("movieCount"),
    MOVIE("movie"),
    REVIEWS_ON_MOVIE_PAGE("reviewsOnMoviePage"),
    USER_REVIEW("userReview"),
    REVIEWS_TO_DISPLAY("reviewsToDisplay"),
    REVIEW_COUNT("reviewCount"),
    VALIDATION_ERRORS("validationErrors"),
    SERVICE_ERROR("serviceError"),
    GLOBAL_ERROR("globalError"),
    SUCCESS_NOTIFICATION("successNotification"),
    REPORT_MESSAGE("reportMessage");
    private final String name;

    Attribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
