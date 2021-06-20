package com.epam.jwd_critics.controller.command.impl.common;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.model.entity.Movie;
import com.epam.jwd_critics.model.service.MovieService;
import com.epam.jwd_critics.model.service.impl.MovieServiceImpl;
import com.epam.jwd_critics.tag.ShowAllMoviesTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OpenAllMoviesPageCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(OpenAllMoviesPageCommand.class);
    private final MovieService movieService = MovieServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse commandResult = new CommandResponse(ServletDestination.ALL_MOVIES, TransferType.FORWARD);

        Integer currentPage = (Integer) req.getAttribute(Attribute.ALL_MOVIES_CURRENT_PAGE);
        String newPageStr = req.getParameter(Parameter.NEW_MOVIE_PAGE);
        if (newPageStr != null) {
            currentPage = Integer.valueOf(newPageStr);
        } else if (currentPage == null) {
            currentPage = 1;
        }
        req.setAttribute(Attribute.ALL_MOVIES_CURRENT_PAGE, currentPage);
        int begin = (currentPage - 1) * ShowAllMoviesTag.MOVIES_PER_PAGE;
        int end = ShowAllMoviesTag.MOVIES_PER_PAGE + begin;
        try {
            List<Movie> moviesToDisplay = movieService.getAllBetween(begin, end);
            req.setAttribute(Attribute.MOVIES_TO_DISPLAY, moviesToDisplay);
            int movieCount = movieService.getCount();
            req.setAttribute(Attribute.MOVIE_COUNT, movieCount);
            if (moviesToDisplay.size() == 0) {
                req.setSessionAttribute(Attribute.REPORT_MESSAGE, "No movies here yet");
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            req.setSessionAttribute(Attribute.SERVICE_ERROR, e.getMessage());
            commandResult.setDestination(ServletDestination.MAIN);
        }
        return commandResult;
    }
}
