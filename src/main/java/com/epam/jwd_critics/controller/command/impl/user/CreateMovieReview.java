package com.epam.jwd_critics.controller.command.impl.user;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.impl.common.OpenMoviePage;
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.message.SuccessMessage;
import com.epam.jwd_critics.service.MovieReviewService;
import com.epam.jwd_critics.service.impl.MovieReviewServiceImpl;
import com.epam.jwd_critics.validation.ConstraintViolation;
import com.epam.jwd_critics.validation.MovieReviewValidator;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateMovieReview implements Command {
    private final MovieReviewService reviewService = MovieReviewServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        String reviewScore = req.getParameter(Parameter.MOVIE_REVIEW_SCORE);
        String reviewText = req.getParameter(Parameter.MOVIE_REVIEW_TEXT);
        String movieIdStr = req.getParameter(Parameter.MOVIE_ID);
        String userIdStr = req.getParameter(Parameter.USER_ID);
        if (reviewScore == null || reviewText == null) {
            req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, Collections.singletonList(ErrorMessage.EMPTY_FIELDS));
        } else if (movieIdStr == null || userIdStr == null) {
            throw new CommandException(ErrorMessage.MISSING_ARGUMENTS);
        } else {
            MovieReviewValidator reviewValidator = new MovieReviewValidator();
            Set<ConstraintViolation> violations = reviewValidator.validateReview(reviewText, reviewScore);
            if (violations.isEmpty()) {
                try {
                    reviewService.create(new MovieReview(reviewText, Integer.parseInt(reviewScore), Integer.parseInt(userIdStr), Integer.parseInt(movieIdStr)));
                    req.setSessionAttribute(Attribute.SUCCESS_NOTIFICATION, SuccessMessage.MOVIE_REVIEW_CREATED);
                } catch (ServiceException e) {
                    req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
                }
            } else {
                req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList()));
            }
        }
        new OpenMoviePage().execute(req);
        return CommandResponse.redirectToPreviousPageOr(ServletDestination.MAIN, req);
    }
}
