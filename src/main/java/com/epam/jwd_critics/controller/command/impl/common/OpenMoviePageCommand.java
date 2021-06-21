package com.epam.jwd_critics.controller.command.impl.common;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.dto.MovieDTO;
import com.epam.jwd_critics.dto.MovieReviewDTO;
import com.epam.jwd_critics.dto.UserDTO;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.service.MovieReviewService;
import com.epam.jwd_critics.service.MovieService;
import com.epam.jwd_critics.service.UserService;
import com.epam.jwd_critics.service.impl.MovieReviewServiceImpl;
import com.epam.jwd_critics.service.impl.MovieServiceImpl;
import com.epam.jwd_critics.service.impl.UserServiceImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OpenMoviePageCommand implements Command {
    private final MovieService movieService = MovieServiceImpl.getInstance();
    private final MovieReviewService reviewService = MovieReviewServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance();


    private final Integer AMOUNT_OF_REVIEW_ON_PAGE = 2;

    @Override
    public CommandResponse execute(CommandRequest req) {
        CommandResponse resp = new CommandResponse(ServletDestination.MOVIE, TransferType.FORWARD);
        String movieIdStr = req.getParameter(Parameter.MOVIE_ID);
        Integer movieId;
        if (movieIdStr == null) {
            MovieDTO movie = (MovieDTO) req.getSessionAttribute(Attribute.MOVIE);
            if (movie == null) {
                req.setSessionAttribute(Attribute.GLOBAL_ERROR, "Empty movie id");
                resp.setDestination(ServletDestination.MAIN);
                resp.setTransferType(TransferType.REDIRECT);
                return resp;
            } else {
                movieId = movie.getId();
            }
        } else{
            movieId = Integer.parseInt(movieIdStr);
        }
        try {
            Optional<Movie> movie = movieService.getEntityById(movieId);
            if (movie.isPresent()) {
                req.setSessionAttribute(Attribute.MOVIE, new MovieDTO(movie.get()));
                UserDTO user = (UserDTO) req.getSessionAttribute(Attribute.USER);
                if (user != null) {
                    Optional<MovieReview> usersReview = reviewService.getEntity(user.getId(), movie.get().getId());
                    usersReview.ifPresent(value -> req.setSessionAttribute(Attribute.USER_REVIEW, value));
                }
                List<MovieReview> reviews = reviewService.getMovieReviewsByMovieId(movieId, 0, AMOUNT_OF_REVIEW_ON_PAGE);
                List<MovieReviewDTO> reviewDTOS = new LinkedList<>();
                for (MovieReview review : reviews) {
                    MovieReviewDTO reviewDTO = new MovieReviewDTO(review);
                    Optional<User> userOfReview = userService.getEntityById(reviewDTO.getUserId());
                    if (userOfReview.isPresent()) {
                        reviewDTO.setUserImagePath(userOfReview.get().getImagePath());
                        reviewDTO.setUserName(userOfReview.get().getFirstName());
                        reviewDTOS.add(reviewDTO);
                    }
                }
                req.setSessionAttribute(Attribute.REVIEWS_ON_MOVIE_PAGE, reviewDTOS);
            } else {
                req.setSessionAttribute(Attribute.GLOBAL_ERROR, "Movie does not exist");
                resp.setDestination(ServletDestination.MAIN);
                resp.setTransferType(TransferType.REDIRECT);
            }
        } catch (ServiceException e) {
            req.setSessionAttribute(Attribute.SERVICE_ERROR, e.getMessage());
            resp.setDestination(ServletDestination.MAIN);
            resp.setTransferType(TransferType.REDIRECT);
        }
        return resp;
    }
}
