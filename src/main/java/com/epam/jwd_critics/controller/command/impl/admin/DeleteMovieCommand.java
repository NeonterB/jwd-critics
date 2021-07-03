package com.epam.jwd_critics.controller.command.impl.admin;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.impl.common.OpenAllMoviesPageCommand;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.message.SuccessMessage;
import com.epam.jwd_critics.service.MovieService;
import com.epam.jwd_critics.service.impl.MovieServiceImpl;

public class DeleteMovieCommand implements Command {
    private final MovieService movieService = MovieServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        String movieIdStr = req.getParameter(Parameter.MOVIE_ID);
        if (movieIdStr == null) {
            throw new CommandException(ErrorMessage.MISSING_ARGUMENTS);
        }
        try {
            movieService.delete(Integer.parseInt(movieIdStr));
            req.removeSessionAttribute(Attribute.MOVIE);
            req.removeSessionAttribute(Attribute.REVIEWS_ON_MOVIE_PAGE);
            req.setSessionAttribute(Attribute.SUCCESS_NOTIFICATION, SuccessMessage.MOVIE_DELETED);
        } catch (ServiceException e) {
            req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
        }
        return new OpenAllMoviesPageCommand().execute(req);
    }
}
