package com.epam.jwd_critics.controller.command;

public enum Attribute {
    USER("user"),
    USER_ID("userId"),
    LANG("lang"),

    ALL_MOVIES_CURRENT_PAGE("allMoviesCurrentPage"),
    ALL_USERS_CURRENT_PAGE("allUsersCurrentPage"),
    REVIEWS_CURRENT_PAGE("reviewsCurrentPage"),
    NEW_PAGE("newPage"),

    CURRENT_PAGE("currentPage"),
    PREVIOUS_PAGE("previousPage"),

    MOVIES_TO_DISPLAY("moviesToDisplay"),
    REVIEWS_TO_DISPLAY("reviewsToDisplay"),
    USERS_TO_DISPLAY("usersToDisplay"),

    MOVIE_COUNT("movieCount"),
    REVIEW_COUNT("reviewCount"),
    USER_COUNT("userCount"),

    MOVIE("movie"),
    REVIEWS_ON_MOVIE_PAGE("reviewsOnMoviePage"),
    USER_REVIEW("userReview"),

    USER_PROFILE("userProfile"),
    REVIEWS_ON_USER_PROFILE_PAGE("reviewsOnUserProfilePage"),

    NEW_IMAGE("newImage"),

    VALIDATION_WARNINGS("validationWarnings"),
    FATAL_NOTIFICATION("fatalNotification"),
    COMMAND_ERROR("commandError"),
    SUCCESS_NOTIFICATION("successNotification"),
    INFO_MESSAGE("infoMessage");

    private final String name;

    Attribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
