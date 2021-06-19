package com.epam.jwd_critics.controller.command.impl.common;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
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
        CommandResponse commandResult = new CommandResponse(ServletDestination.ALL_MOVIES, TransferType.REDIRECT);

        Integer currentPage = (Integer) req.getSessionAttribute(Attribute.ALL_MOVIES_CURRENT_PAGE);
        Integer newPage = (Integer) req.getAttribute(Attribute.NEW_PAGE);
        if (newPage != null) {
            currentPage = newPage;
        } else if (currentPage == null) {
            currentPage = 1;
        }
        req.setSessionAttribute(Attribute.ALL_MOVIES_CURRENT_PAGE, currentPage);
        int begin = (currentPage - 1) * ShowAllMoviesTag.MOVIES_PER_PAGE_NUMBER;
        int end = ShowAllMoviesTag.MOVIES_PER_PAGE_NUMBER + begin;
        try {
            List<Movie> moviesToDisplay = movieService.getAllBetween(begin, end);
            req.setSessionAttribute(Attribute.MOVIES_TO_DISPLAY, moviesToDisplay);
            int movieCount = movieService.getCount();
            req.setSessionAttribute(Attribute.MOVIE_COUNT, movieCount);
            if (moviesToDisplay.size() == 0) {
                //todo ?
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            throw new CommandException(e);
        }
        return commandResult;
    }
}
