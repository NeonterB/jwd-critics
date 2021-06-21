package com.epam.jwd_critics.controller.command.impl.common;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.dto.MovieDTO;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.service.MovieReviewService;
import com.epam.jwd_critics.service.MovieService;
import com.epam.jwd_critics.service.impl.MovieReviewServiceImpl;
import com.epam.jwd_critics.service.impl.MovieServiceImpl;

import java.util.Optional;

public class OpenMoviePageCommand implements Command {
    private final MovieService movieService = MovieServiceImpl.getInstance();
    private final MovieReviewService reviewService = MovieReviewServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) {
        CommandResponse commandResult = new CommandResponse(ServletDestination.MOVIE, TransferType.FORWARD);
        String movieIdStr = req.getParameter(Parameter.MOVIE_ID);
        if (movieIdStr == null) {
            req.setSessionAttribute(Attribute.GLOBAL_ERROR, "Empty movie id");
            commandResult.setDestination(ServletDestination.MAIN);
            commandResult.setTransferType(TransferType.REDIRECT);
        } else {
            try {
                Optional<Movie> movie = movieService.getEntityById(Integer.valueOf(movieIdStr));
                if (movie.isPresent()) {
                    req.setSessionAttribute(Attribute.MOVIE, new MovieDTO(movie.get()));
                    Integer userId = (Integer) req.getSessionAttribute(Attribute.USER_ID);
                    if (userId != null) {
                        Optional<MovieReview> usersReview = reviewService.getEntity(userId, movie.get().getId());
                        usersReview.ifPresent(value -> req.setSessionAttribute(Attribute.USER_REVIEW, value));
                    }
                } else {
                    req.setSessionAttribute(Attribute.GLOBAL_ERROR, "Movie does not exist");
                    commandResult.setDestination(ServletDestination.MAIN);
                    commandResult.setTransferType(TransferType.REDIRECT);
                }
            } catch (ServiceException e) {
                req.setSessionAttribute(Attribute.SERVICE_ERROR, e.getMessage());
                commandResult.setDestination(ServletDestination.MAIN);
                commandResult.setTransferType(TransferType.REDIRECT);
            }
        }
        return new GetMovieReviewsCommand().execute(req);
    }
}
