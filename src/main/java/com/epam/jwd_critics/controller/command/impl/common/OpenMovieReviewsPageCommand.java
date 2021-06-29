package com.epam.jwd_critics.controller.command.impl.common;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.dto.MovieReviewDTO;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.service.MovieReviewService;
import com.epam.jwd_critics.service.UserService;
import com.epam.jwd_critics.service.impl.MovieReviewServiceImpl;
import com.epam.jwd_critics.service.impl.UserServiceImpl;
import com.epam.jwd_critics.tag.ShowReviewsTag;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class OpenMovieReviewsPageCommand implements Command {
    private final MovieReviewService reviewService = MovieReviewServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse resp = new CommandResponse(ServletDestination.REVIEWS, TransferType.FORWARD);
        int movieId;
        String movieIdStr = req.getParameter(Parameter.MOVIE_ID);
        if (movieIdStr == null) {
            Movie movie = (Movie) req.getSessionAttribute(Attribute.MOVIE);
            if (movie == null) {
                throw new CommandException(ErrorMessage.MISSING_ARGUMENTS);
            }
            movieId = movie.getId();
        } else {
            movieId = Integer.parseInt(movieIdStr);
        }
        try {
            Integer currentPage = (Integer) req.getSessionAttribute(Attribute.REVIEWS_CURRENT_PAGE);
            String newPageStr = req.getParameter(Parameter.NEW_REVIEWS_PAGE);
            int reviewCount = reviewService.getCountByMovieId(movieId);
            req.setSessionAttribute(Attribute.REVIEW_COUNT, reviewCount);
            if (newPageStr != null) {
                currentPage = Integer.parseInt(newPageStr);
            } else if (currentPage == null) {
                currentPage = 1;
            } else if (currentPage > 1 && (currentPage - 1) * ShowReviewsTag.REVIEWS_PER_PAGE >= reviewCount) {
                currentPage--;
            }
            req.setSessionAttribute(Attribute.REVIEWS_CURRENT_PAGE, currentPage);
            int begin = (currentPage - 1) * ShowReviewsTag.REVIEWS_PER_PAGE;
            int end = ShowReviewsTag.REVIEWS_PER_PAGE + begin;

            List<MovieReview> reviews = reviewService.getMovieReviewsByMovieId(movieId, begin, end);
            List<MovieReviewDTO> reviewDTOS = new LinkedList<>();
            for (MovieReview review : reviews) {
                Optional<User> userOfReview = userService.getEntityById(review.getUserId());
                userOfReview.ifPresent(value -> reviewDTOS.add(new MovieReviewDTO(review, value)));
            }
            req.setSessionAttribute(Attribute.REVIEWS_TO_DISPLAY, reviewDTOS);
            if (reviewDTOS.size() == 0) {
                req.setSessionAttribute(Attribute.INFO_MESSAGE, "No reviews here yet");
            }
        } catch (ServiceException e) {
            req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
            resp = CommandResponse.redirectToPreviousPageOr(ServletDestination.MAIN, req);
        }
        return resp;
    }
}
