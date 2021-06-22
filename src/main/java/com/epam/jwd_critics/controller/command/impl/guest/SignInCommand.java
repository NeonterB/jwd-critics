package com.epam.jwd_critics.controller.command.impl.guest;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.dto.MovieDTO;
import com.epam.jwd_critics.dto.UserDTO;
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.ServiceException;
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
    public CommandResponse execute(CommandRequest req) {
        CommandResponse response = new CommandResponse(ServletDestination.SIGN_IN, TransferType.REDIRECT);

        String login = req.getParameter(Parameter.LOGIN);
        String password = req.getParameter(Parameter.PASSWORD);
        if (login == null || password == null) {
            req.setSessionAttribute(Attribute.VALIDATION_ERRORS, Collections.singletonList("Sign in fields can't be empty"));
        } else {
            UserValidator userValidator = new UserValidator();
            Set<ConstraintViolation> violations = userValidator.validateLogInData(login, password);
            if (violations.isEmpty()) {
                try {
                    User user = userService.login(login, password);
                    req.setSessionAttribute(Attribute.USER, new UserDTO(user));
                    String page = req.getParameter(Parameter.CURRENT_PAGE);
                    if (page != null) {
                        response.setDestination(() -> page);
                        if (page.equals(ServletDestination.MOVIE.getPath())) {
                            MovieDTO movie = (MovieDTO) req.getSessionAttribute(Attribute.MOVIE);
                            if (movie != null) {
                                Optional<MovieReview> usersReview = reviewService.getEntity(user.getId(), movie.getId());
                                usersReview.ifPresent(value -> req.setSessionAttribute(Attribute.USER_REVIEW, value));
                            } else {
                                req.setSessionAttribute(Attribute.GLOBAL_ERROR, "Empty movie");
                            }
                        }
                    } else {
                        response.setDestination(ServletDestination.MAIN);
                    }
                } catch (ServiceException e) {
                    req.setSessionAttribute(Attribute.SERVICE_ERROR, e.getMessage());
                }
            } else {
                req.setSessionAttribute(Attribute.VALIDATION_ERRORS, violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList()));
            }
        }
        return response;
    }
}
