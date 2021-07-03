package com.epam.jwd_critics.controller.command.impl.common;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.message.InfoMessage;
import com.epam.jwd_critics.service.MovieService;
import com.epam.jwd_critics.service.impl.MovieServiceImpl;

import java.util.List;

public class FindMoviesCommand implements Command {
    private final MovieService movieService = MovieServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) {
        CommandResponse resp = new CommandResponse(ServletDestination.MAIN, TransferType.REDIRECT);

        String movieTitle = req.getParameter(Parameter.MOVIE_NAME);
        if (movieTitle == null) {
            req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, ErrorMessage.EMPTY_FIELDS);
            return resp;
        }
        try {
            List<Movie> movies = movieService.getMoviesByNamePart(movieTitle);
            if (movies.size() == 0) {
                req.setSessionAttribute(Attribute.INFO_MESSAGE, InfoMessage.NO_MOVIES_FOUND);
            } else {
                req.setSessionAttribute(Attribute.FOUND_MOVIES, movies);
            }
        } catch (ServiceException e) {
            req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
        }
        return resp;
    }
}
