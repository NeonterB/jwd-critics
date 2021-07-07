package com.epam.jwd_critics.controller.command.impl.common;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.dto.MovieReviewDTO;
import com.epam.jwd_critics.dto.UserDTO;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.service.MovieReviewService;
import com.epam.jwd_critics.service.MovieService;
import com.epam.jwd_critics.service.UserService;
import com.epam.jwd_critics.service.impl.MovieReviewServiceImpl;
import com.epam.jwd_critics.service.impl.MovieServiceImpl;
import com.epam.jwd_critics.service.impl.UserServiceImpl;
import com.epam.jwd_critics.util.ApplicationPropertiesKeys;
import com.epam.jwd_critics.util.ApplicationPropertiesLoader;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class OpenMoviePageCommand implements Command {
    private final MovieService movieService = MovieServiceImpl.getInstance();
    private final MovieReviewService reviewService = MovieReviewServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance();

    private final int amountOfReviewsOnPage = Integer.parseInt(ApplicationPropertiesLoader.get(ApplicationPropertiesKeys.WEBAPP_AMOUNT_OF_REVIEWS_ON_PAGE));

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse resp = CommandResponse.redirectToPreviousPageOr(ServletDestination.MAIN, req);
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
            Optional<Movie> movie = movieService.getEntityById(movieId);
            if (movie.isPresent()) {
                UserDTO user = (UserDTO) req.getSessionAttribute(Attribute.USER);
                List<MovieReview> reviews = reviewService.getMovieReviewsByMovieId(movieId, 0, amountOfReviewsOnPage);
                List<MovieReviewDTO> reviewDTOS = new LinkedList<>();
                for (MovieReview review : reviews) {
                    Optional<User> userOfReview = userService.getEntityById(review.getUserId());
                    userOfReview.ifPresent(value -> reviewDTOS.add(new MovieReviewDTO(review, value)));
                }
                req.setSessionAttribute(Attribute.MOVIE, movie.get());
                req.setSessionAttribute(Attribute.REVIEWS_ON_MOVIE_PAGE, reviewDTOS);
                if (user != null) {
                    Optional<MovieReview> userReview = reviewService.getEntity(user.getId(), movieId);
                    if (userReview.isPresent()) {
                        req.setSessionAttribute(Attribute.USER_REVIEW, userReview.get());
                    } else {
                        req.removeSessionAttribute(Attribute.USER_REVIEW);
                    }
                }
                resp = new CommandResponse(ServletDestination.MOVIE, TransferType.FORWARD);
            } else {
                req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, ErrorMessage.MOVIE_DOES_NOT_EXIST);
            }
        } catch (ServiceException e) {
            req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
        }
        return resp;
    }
}
