package com.epam.jwd_critics.controller.command;

public enum ServletDestination implements Destination {
    MAIN("/pages/common/main.jsp"),
    SIGN_IN("/pages/guest/sign_in.jsp"),
    FORGOT_PASSWORD("/pages/guest/forgot_password.jsp"),
    PASSWORD_RECOVERY("/pages/guest/password_recovery.jsp"),
    ALL_MOVIES("/pages/common/all_movies.jsp"),
    ALL_CELEBRITIES("/pages/common/all_celebrities.jsp"),
    ALL_USERS("/pages/admin/all_users.jsp"),
    USER_PROFILE("/pages/common/user_profile.jsp"),
    CELEBRITY_PROFILE("/pages/common/celebrity_profile.jsp"),
    UPDATE_USER("/pages/user/update_user.jsp"),
    UPDATE_CELEBRITY("/pages/admin/update_celebrity.jsp"),
    UPDATE_MOVIE("/pages/admin/update_movie.jsp"),
    REVIEWS("/pages/common/reviews.jsp"),
    MOVIE("/pages/common/movie.jsp"),
    ERROR_404("/pages/error/404.jsp"),
    ERROR_500("/pages/error/500.jsp");
    private final String path;

    ServletDestination(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    public final static String ADMIN_URL = "/pages/admin";
    public final static String USER_URL = "/pages/user";
    public final static String GUEST_URL = "/pages/guest";
    public final static String ERROR_URL = "/pages/error";
    public final static String COMPONENT_URL = "/pages/components";
}
