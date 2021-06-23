package com.epam.jwd_critics.controller.command.impl.guest;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.dto.MovieDTO;
import com.epam.jwd_critics.dto.UserDTO;
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.service.MovieReviewService;
import com.epam.jwd_critics.service.UserService;
import com.epam.jwd_critics.service.impl.MovieReviewServiceImpl;
import com.epam.jwd_critics.service.impl.UserServiceImpl;
import com.epam.jwd_critics.validation.ConstraintViolation;
import com.epam.jwd_critics.validation.UserValidator;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SignInCommand implements Command {
    private final UserService userService = UserServiceImpl.getInstance();
    private final MovieReviewService reviewService = MovieReviewServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse resp = CommandResponse.redirectToPreviousPageOr(ServletDestination.MAIN, req);

        String login = req.getParameter(Parameter.LOGIN);
        String password = req.getParameter(Parameter.PASSWORD);
        if (login == null || password == null) {
            req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, Collections.singletonList(ErrorMessage.EMPTY_SIGN_IN_FIELDS));
            resp.setDestination(ServletDestination.SIGN_IN);
        } else {
            UserValidator userValidator = new UserValidator();
            Set<ConstraintViolation> violations = userValidator.validateLogInData(login, password);
            if (violations.isEmpty()) {
                try {
                    User user = userService.login(login, password);
                    req.setSessionAttribute(Attribute.USER, new UserDTO(user));
                    String page = req.getParameter(Parameter.CURRENT_PAGE);
                    if (page != null) {
                        if (page.equals(ServletDestination.MOVIE.getPath())) {
                            MovieDTO movie = (MovieDTO) req.getSessionAttribute(Attribute.MOVIE);
                            if (movie != null) {
                                Optional<MovieReview> usersReview = reviewService.getEntity(user.getId(), movie.getId());
                                usersReview.ifPresent(value -> req.setSessionAttribute(Attribute.USER_REVIEW, value));
                            } else {
                                throw new CommandException(ErrorMessage.MISSING_ARGUMENTS);
                            }
                        }
                    }
                } catch (ServiceException e) {
                    req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
                    resp.setDestination(ServletDestination.SIGN_IN);
                }
            } else {
                req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList()));
                resp.setDestination(ServletDestination.SIGN_IN);
            }
        }
        return resp;
    }
}
