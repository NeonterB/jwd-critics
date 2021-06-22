package com.epam.jwd_critics.controller.command;

public enum Attribute {
    USER("user"),
    LANG("lang"),
    ALL_MOVIES_CURRENT_PAGE("allMoviesCurrentPage"),
    ALL_USERS_CURRENT_PAGE("allUsersCurrentPage"),
    REVIEWS_CURRENT_PAGE("reviewsCurrentPage"),
    NEW_PAGE("newPage"),
    CURRENT_PAGE("currentPage"),
    MOVIES_TO_DISPLAY("allMoviesList"),
    REVIEWS_TO_DISPLAY("reviewsToDisplay"),
    USERS_TO_DISPLAY("usersToDisplay"),
    MOVIE_COUNT("movieCount"),
    REVIEW_COUNT("reviewCount"),
    USER_COUNT("userCount"),
    MOVIE("movie"),
    REVIEWS_ON_MOVIE_PAGE("reviewsOnMoviePage"),
    USER_REVIEW("userReview"),
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
