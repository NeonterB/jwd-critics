package com.epam.jwd_critics.controller.command;

import com.epam.jwd_critics.controller.command.impl.admin.OpenAllUsersPageCommand;
import com.epam.jwd_critics.controller.command.impl.common.ChangeLocaleCommand;
import com.epam.jwd_critics.controller.command.impl.common.OpenAllMoviesPageCommand;
import com.epam.jwd_critics.controller.command.impl.common.OpenMoviePageCommand;
import com.epam.jwd_critics.controller.command.impl.common.OpenMovieReviewsPageCommand;
import com.epam.jwd_critics.controller.command.impl.guest.SignInCommand;
import com.epam.jwd_critics.controller.command.impl.guest.RegisterCommand;
import com.epam.jwd_critics.controller.command.impl.common.OpenSignInPageCommand;
import com.epam.jwd_critics.controller.command.impl.common.OpenMainPageCommand;
import com.epam.jwd_critics.controller.command.impl.user.CreateMovieReviewCommand;
import com.epam.jwd_critics.controller.command.impl.user.DeleteMovieReviewCommand;
import com.epam.jwd_critics.controller.command.impl.user.SignOutCommand;
import com.epam.jwd_critics.controller.command.impl.user.UpdateMovieReviewCommand;
import com.epam.jwd_critics.entity.Role;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum CommandInstance {
    OPEN_MAIN(new OpenMainPageCommand(), Role.values()),
    OPEN_SIGN_IN(new OpenSignInPageCommand(), Role.GUEST),
    OPEN_ALL_MOVIES(new OpenAllMoviesPageCommand(), Role.values()),
    OPEN_ALL_USERS(new OpenAllUsersPageCommand(), true, Role.ADMIN),
    OPEN_MOVIE(new OpenMoviePageCommand(), Role.values()),
    OPEN_MOVIE_REVIEWS(new OpenMovieReviewsPageCommand(), Role.values()),
    CREATE_MOVIE_REVIEW(new CreateMovieReviewCommand(), true, Role.USER, Role.ADMIN),
    UPDATE_MOVIE_REVIEW(new UpdateMovieReviewCommand(), true, Role.USER, Role.ADMIN),
    DELETE_MOVIE_REVIEW(new DeleteMovieReviewCommand(), true, Role.USER, Role.ADMIN),
    CHANGE_LANGUAGE(new ChangeLocaleCommand(), Role.values()),
    SIGN_IN(new SignInCommand(), Role.GUEST),
    REGISTER(new RegisterCommand(), Role.GUEST),
    SIGN_OUT(new SignOutCommand(), Role.ADMIN, Role.USER);
    private final Command command;
    private final List<Role> allowedRoles = new LinkedList<>();
    private boolean userMustBeActive = false;

    CommandInstance(Command command, Role ...roles) {
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
