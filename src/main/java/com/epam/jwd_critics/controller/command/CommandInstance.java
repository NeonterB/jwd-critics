package com.epam.jwd_critics.controller.command;

import com.epam.jwd_critics.controller.command.impl.admin.OpenAllUsersPageCommand;
import com.epam.jwd_critics.controller.command.impl.admin.OpenUpdateCelebrityPageCommand;
import com.epam.jwd_critics.controller.command.impl.admin.OpenUpdateMoviePageCommand;
import com.epam.jwd_critics.controller.command.impl.admin.UpdateCelebrityCommand;
import com.epam.jwd_critics.controller.command.impl.admin.UpdateMovieCommand;
import com.epam.jwd_critics.controller.command.impl.admin.UpdateUserStatusCommand;
import com.epam.jwd_critics.controller.command.impl.common.ChangeLocaleCommand;
import com.epam.jwd_critics.controller.command.impl.common.OpenAllCelebritiesPageCommand;
import com.epam.jwd_critics.controller.command.impl.common.OpenAllMoviesPageCommand;
import com.epam.jwd_critics.controller.command.impl.common.OpenCelebrityProfilePageCommand;
import com.epam.jwd_critics.controller.command.impl.common.OpenMainPageCommand;
import com.epam.jwd_critics.controller.command.impl.common.OpenMoviePageCommand;
import com.epam.jwd_critics.controller.command.impl.common.OpenMovieReviewsPageCommand;
import com.epam.jwd_critics.controller.command.impl.common.OpenUserProfilePageCommand;
import com.epam.jwd_critics.controller.command.impl.guest.OpenForgotPasswordPageCommand;
import com.epam.jwd_critics.controller.command.impl.guest.OpenPasswordRecoveryPageCommand;
import com.epam.jwd_critics.controller.command.impl.guest.OpenSignInPageCommand;
import com.epam.jwd_critics.controller.command.impl.guest.RegisterCommand;
import com.epam.jwd_critics.controller.command.impl.guest.SendRecoveryMailCommand;
import com.epam.jwd_critics.controller.command.impl.guest.SignInCommand;
import com.epam.jwd_critics.controller.command.impl.guest.UpdatePasswordCommand;
import com.epam.jwd_critics.controller.command.impl.user.ActivateUserCommand;
import com.epam.jwd_critics.controller.command.impl.user.CreateMovieReviewCommand;
import com.epam.jwd_critics.controller.command.impl.user.DeleteMovieReviewCommand;
import com.epam.jwd_critics.controller.command.impl.user.DeleteUserCommand;
import com.epam.jwd_critics.controller.command.impl.user.OpenUpdateUserPageCommand;
import com.epam.jwd_critics.controller.command.impl.user.SignOutCommand;
import com.epam.jwd_critics.controller.command.impl.user.UpdateMovieReviewCommand;
import com.epam.jwd_critics.controller.command.impl.user.UpdateUserCommand;
import com.epam.jwd_critics.controller.command.impl.user.UploadPictureCommand;
import com.epam.jwd_critics.entity.Role;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum CommandInstance {
    OPEN_MAIN(new OpenMainPageCommand(), Role.values()),
    OPEN_SIGN_IN(new OpenSignInPageCommand(), Role.GUEST),
    SIGN_IN(new SignInCommand(), Role.GUEST),
    REGISTER(new RegisterCommand(), Role.GUEST),
    SIGN_OUT(new SignOutCommand(), Role.ADMIN, Role.USER),

    OPEN_FORGOT_PASSWORD(new OpenForgotPasswordPageCommand(), Role.GUEST),
    SEND_RECOVERY_MAIL(new SendRecoveryMailCommand(), Role.GUEST),
    OPEN_PASSWORD_RECOVERY(new OpenPasswordRecoveryPageCommand(), Role.GUEST),
    UPDATE_PASSWORD(new UpdatePasswordCommand(), Role.GUEST),

    OPEN_ALL_MOVIES(new OpenAllMoviesPageCommand(), Role.values()),
    OPEN_ALL_CELEBRITIES(new OpenAllCelebritiesPageCommand(), Role.values()),
    OPEN_ALL_USERS(new OpenAllUsersPageCommand(), true, Role.ADMIN),

    OPEN_MOVIE(new OpenMoviePageCommand(), Role.values()),
    OPEN_UPDATE_MOVIE(new OpenUpdateMoviePageCommand(), true, Role.ADMIN),
    UPDATE_MOVIE(new UpdateMovieCommand(), true, Role.ADMIN),

    OPEN_USER_PROFILE(new OpenUserProfilePageCommand(), Role.values()),
    OPEN_UPDATE_USER(new OpenUpdateUserPageCommand(), Role.USER, Role.ADMIN),
    UPDATE_USER(new UpdateUserCommand(), true, Role.ADMIN, Role.USER),
    UPDATE_USER_STATUS(new UpdateUserStatusCommand(), true, Role.ADMIN),
    DELETE_USER(new DeleteUserCommand(), Role.USER),
    ACTIVATE_USER(new ActivateUserCommand(), Role.USER),

    OPEN_CELEBRITY_PROFILE(new OpenCelebrityProfilePageCommand(), Role.values()),
    OPEN_UPDATE_CELEBRITY(new OpenUpdateCelebrityPageCommand(), true, Role.ADMIN),
    UPDATE_CELEBRITY(new UpdateCelebrityCommand(), true, Role.ADMIN),

    OPEN_MOVIE_REVIEWS(new OpenMovieReviewsPageCommand(), Role.values()),
    CREATE_MOVIE_REVIEW(new CreateMovieReviewCommand(), true, Role.USER, Role.ADMIN),
    UPDATE_MOVIE_REVIEW(new UpdateMovieReviewCommand(), true, Role.USER, Role.ADMIN),
    DELETE_MOVIE_REVIEW(new DeleteMovieReviewCommand(), true, Role.USER, Role.ADMIN),

    UPLOAD_PICTURE(new UploadPictureCommand(), Role.USER, Role.ADMIN),
    CHANGE_LANGUAGE(new ChangeLocaleCommand(), Role.values());

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
