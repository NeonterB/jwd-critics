package com.epam.jwd_critics.controller.command.impl.user;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.impl.common.OpenMoviePageCommand;
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.message.SuccessMessage;
import com.epam.jwd_critics.service.MovieReviewService;
import com.epam.jwd_critics.service.impl.MovieReviewServiceImpl;
import com.epam.jwd_critics.validation.ConstraintViolation;
import com.epam.jwd_critics.validation.MovieReviewValidator;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UpdateMovieReviewCommand implements Command {
    private final MovieReviewService reviewService = MovieReviewServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        String reviewScore = req.getParameter(Parameter.MOVIE_REVIEW_SCORE);
        String reviewText = req.getParameter(Parameter.MOVIE_REVIEW_TEXT);
        String reviewIdStr = req.getParameter(Parameter.REVIEW_ID);
        if (reviewScore == null || reviewText == null) {
            req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, ErrorMessage.EMPTY_FIELDS);
        } else if (reviewIdStr == null) {
            throw new CommandException(ErrorMessage.MISSING_ARGUMENTS);
        } else {
            MovieReviewValidator reviewValidator = new MovieReviewValidator();
            Set<ConstraintViolation> violations = reviewValidator.validateReview(reviewText, reviewScore);

            if (violations.isEmpty()) {
                try {
                    Optional<MovieReview> reviewToUpdate = reviewService.getEntityById(Integer.parseInt(reviewIdStr));
                    if (reviewToUpdate.isPresent()){
                        reviewToUpdate.get().setScore(Integer.parseInt(reviewScore));
                        reviewToUpdate.get().setText(reviewText);
                        reviewService.update(reviewToUpdate.get());
                        req.setSessionAttribute(Attribute.SUCCESS_NOTIFICATION, SuccessMessage.MOVIE_REVIEW_UPDATED);
                    } else {
                        req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, ErrorMessage.MOVIE_REVIEW_DOES_NOT_EXIST);
                    }
                } catch (ServiceException e) {
                    req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
                }
            } else {
                req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList()));
            }
        }
        new OpenMoviePageCommand().execute(req);
        return CommandResponse.redirectToPreviousPageOr(ServletDestination.MOVIE, req);
    }
}
