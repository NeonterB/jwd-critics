package com.epam.jwd_critics.controller.command;

import com.epam.jwd_critics.controller.command.impl.admin.OpenAllUsersPage;
import com.epam.jwd_critics.controller.command.impl.admin.UpdateUserStatus;
import com.epam.jwd_critics.controller.command.impl.common.ChangeLocale;
import com.epam.jwd_critics.controller.command.impl.common.OpenAllMoviesPage;
import com.epam.jwd_critics.controller.command.impl.common.OpenMainPage;
import com.epam.jwd_critics.controller.command.impl.common.OpenMoviePage;
import com.epam.jwd_critics.controller.command.impl.common.OpenMovieReviewsPage;
import com.epam.jwd_critics.controller.command.impl.common.OpenUserProfilePage;
import com.epam.jwd_critics.controller.command.impl.guest.OpenForgotPasswordPage;
import com.epam.jwd_critics.controller.command.impl.guest.OpenPasswordRecoveryPage;
import com.epam.jwd_critics.controller.command.impl.guest.OpenSignInPage;
import com.epam.jwd_critics.controller.command.impl.guest.Register;
import com.epam.jwd_critics.controller.command.impl.guest.SendRecoveryMail;
import com.epam.jwd_critics.controller.command.impl.guest.SignIn;
import com.epam.jwd_critics.controller.command.impl.guest.UpdatePassword;
import com.epam.jwd_critics.controller.command.impl.user.ActivateUser;
import com.epam.jwd_critics.controller.command.impl.user.CreateMovieReview;
import com.epam.jwd_critics.controller.command.impl.user.DeleteMovieReview;
import com.epam.jwd_critics.controller.command.impl.user.DeleteUser;
import com.epam.jwd_critics.controller.command.impl.user.OpenUpdateUserPage;
import com.epam.jwd_critics.controller.command.impl.user.SignOut;
import com.epam.jwd_critics.controller.command.impl.user.UpdateMovieReview;
import com.epam.jwd_critics.controller.command.impl.user.UpdateUser;
import com.epam.jwd_critics.controller.command.impl.user.UploadPicture;
import com.epam.jwd_critics.entity.Role;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum CommandInstance {
    OPEN_MAIN(new OpenMainPage(), Role.values()),
    OPEN_SIGN_IN(new OpenSignInPage(), Role.GUEST),
    OPEN_FORGOT_PASSWORD(new OpenForgotPasswordPage(), Role.GUEST),
    SEND_RECOVERY_MAIL(new SendRecoveryMail(), Role.GUEST),
    OPEN_PASSWORD_RECOVERY(new OpenPasswordRecoveryPage(), Role.GUEST),
    UPDATE_PASSWORD(new UpdatePassword(), Role.GUEST),
    OPEN_ALL_MOVIES(new OpenAllMoviesPage(), Role.values()),
    OPEN_ALL_USERS(new OpenAllUsersPage(), true, Role.ADMIN),
    OPEN_MOVIE(new OpenMoviePage(), Role.values()),
    OPEN_USER_PROFILE(new OpenUserProfilePage(), Role.values()),
    OPEN_UPDATE_USER(new OpenUpdateUserPage(), Role.USER, Role.ADMIN),
    OPEN_MOVIE_REVIEWS(new OpenMovieReviewsPage(), Role.values()),
    CREATE_MOVIE_REVIEW(new CreateMovieReview(), true, Role.USER, Role.ADMIN),
    UPDATE_MOVIE_REVIEW(new UpdateMovieReview(), true, Role.USER, Role.ADMIN),
    DELETE_MOVIE_REVIEW(new DeleteMovieReview(), true, Role.USER, Role.ADMIN),
    UPDATE_USER_STATUS(new UpdateUserStatus(), true, Role.ADMIN),
    UPDATE_USER(new UpdateUser(), true, Role.ADMIN, Role.USER),
    UPLOAD_PICTURE(new UploadPicture(), true, Role.USER, Role.ADMIN),
    CHANGE_LANGUAGE(new ChangeLocale(), Role.values()),
    SIGN_IN(new SignIn(), Role.GUEST),
    REGISTER(new Register(), Role.GUEST),
    SIGN_OUT(new SignOut(), Role.ADMIN, Role.USER),
    DELETE_USER(new DeleteUser(), Role.USER),
    ACTIVATE_USER(new ActivateUser(), false, Role.USER);

    private final Command command;
    private final List<Role> allowedRoles = new LinkedList<>();
    private boolean userMustBeActive = false;

    CommandInstance(Command command, Role... roles) {
        this.command = command;
        this.allowedRoles.addAll(Arrays.asList(roles));
    }

    CommandInstance(Command command, boolean mustBeActive, Role ...roles) {
        this.command = command;
        this.userMustBeActive = mustBeActive;
        this.allowedRoles.addAll(Arrays.asList(roles));
    }

    public boolean isRoleAllowed(Role role){
        return allowedRoles.contains(role);
    }

    public boolean isUserMustBeActive() {
        return userMustBeActive;
    }

    public static Command commandOf(String commandName) {
        for (CommandInstance v : values()) {
            if (v.name().equalsIgnoreCase(commandName)) {
                return v.command;
            }
        }
        return OPEN_MAIN.command;
    }
}
