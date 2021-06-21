package com.epam.jwd_critics.controller.command.impl.user;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.impl.common.OpenMoviePageCommand;
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.service.MovieReviewService;
import com.epam.jwd_critics.service.impl.MovieReviewServiceImpl;
import com.epam.jwd_critics.validation.ConstraintViolation;
import com.epam.jwd_critics.validation.MovieReviewValidator;

import java.util.Set;
import java.util.stream.Collectors;

public class CreateMovieReviewCommand implements Command {
    private final MovieReviewService reviewService = MovieReviewServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) {
        String reviewScore = req.getParameter(Parameter.MOVIE_REVIEW_SCORE);
        String reviewText = req.getParameter(Parameter.MOVIE_REVIEW_TEXT);
        String movieIdStr = req.getParameter(Parameter.MOVIE_ID);
        String userIdStr = req.getParameter(Parameter.USER_ID);
        if (reviewScore == null || reviewText == null || movieIdStr == null || userIdStr == null) {
            req.setSessionAttribute(Attribute.VALIDATION_ERRORS, "Empty review fields");
        } else {
            MovieReviewValidator reviewValidator = new MovieReviewValidator();
            Set<ConstraintViolation> violations = reviewValidator.validateReview(userIdStr, movieIdStr, reviewText, reviewScore);

            if (violations.isEmpty()) {
                try {
                    reviewService.create(new MovieReview(reviewText, Integer.valueOf(reviewScore), Integer.valueOf(userIdStr), Integer.valueOf(movieIdStr)));
                } catch (ServiceException e) {
                    req.setSessionAttribute(Attribute.SERVICE_ERROR, e.getMessage());
                }
            } else {
                req.setSessionAttribute(Attribute.VALIDATION_ERRORS, violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList()));
            }
        }
        return new OpenMoviePageCommand().execute(req);
    }
}
