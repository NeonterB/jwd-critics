package com.epam.jwd_critics.controller.command.impl.user;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.controller.command.impl.common.OpenMoviePageCommand;
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.service.MovieReviewService;
import com.epam.jwd_critics.service.impl.MovieReviewServiceImpl;
import com.epam.jwd_critics.validation.ConstraintViolation;
import com.epam.jwd_critics.validation.MovieReviewValidator;



import java.util.Set;
import java.util.stream.Collectors;

public class UpdateMovieReviewCommand implements Command {
    private final MovieReviewService reviewService = MovieReviewServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        String reviewScore = req.getParameter(Parameter.MOVIE_REVIEW_SCORE);
        String reviewText = req.getParameter(Parameter.MOVIE_REVIEW_TEXT);
        String movieIdStr = req.getParameter(Parameter.MOVIE_ID);
        String userIdStr = req.getParameter(Parameter.USER_ID);
        String reviewIdStr = req.getParameter(Parameter.REVIEW_ID);
        if (reviewScore == null || reviewText == null) {
            req.setSessionAttribute(Attribute.VALIDATION_ERRORS, "Empty review fields");
        } else if (userIdStr == null) {
            req.setSessionAttribute(Attribute.GLOBAL_ERROR, "Empty user id");
        } else if (movieIdStr == null) {
            req.setSessionAttribute(Attribute.GLOBAL_ERROR, "Empty movie id");
        } else if (reviewIdStr == null) {
            req.setSessionAttribute(Attribute.GLOBAL_ERROR, "Empty review id");
        } else {
            MovieReviewValidator reviewValidator = new MovieReviewValidator();
            Set<ConstraintViolation> violations = reviewValidator.validateReview(userIdStr, movieIdStr, reviewText, reviewScore);

            if (violations.isEmpty()) {
                try {
                    reviewService.update(new MovieReview(Integer.valueOf(reviewIdStr), reviewText, Integer.valueOf(reviewScore), Integer.valueOf(userIdStr), Integer.valueOf(movieIdStr)));
                    req.setSessionAttribute(Attribute.SUCCESS_NOTIFICATION, "Review updated");
                } catch (ServiceException e) {
                    req.setSessionAttribute(Attribute.SERVICE_ERROR, e.getMessage());
                }
            } else {
                req.setSessionAttribute(Attribute.VALIDATION_ERRORS, violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList()));
            }
        }
        new OpenMoviePageCommand().execute(req);
        return new CommandResponse(ServletDestination.MOVIE, TransferType.REDIRECT);
    }
}
