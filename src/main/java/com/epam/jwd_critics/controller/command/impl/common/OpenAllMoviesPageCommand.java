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
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.InfoMessage;
import com.epam.jwd_critics.service.MovieService;
import com.epam.jwd_critics.service.impl.MovieServiceImpl;
import com.epam.jwd_critics.tag.ShowAllMoviesTag;

import java.util.List;
import java.util.stream.Collectors;

public class OpenAllMoviesPageCommand implements Command {
    private final MovieService movieService = MovieServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse resp = new CommandResponse(ServletDestination.ALL_MOVIES, TransferType.FORWARD);

        Integer currentPage = (Integer) req.getSessionAttribute(Attribute.ALL_MOVIES_CURRENT_PAGE);
        String newPageStr = req.getParameter(Parameter.NEW_MOVIES_PAGE);
        try {
            int movieCount = movieService.getCount();
            if (newPageStr != null) {
                currentPage = Integer.valueOf(newPageStr);
            } else if (currentPage == null) {
                currentPage = 1;
            }
            while (currentPage > 1 && (currentPage - 1) * ShowAllMoviesTag.getMoviesPerPage() >= movieCount) {
                currentPage--;
            }
            req.setSessionAttribute(Attribute.ALL_MOVIES_CURRENT_PAGE, currentPage);
            int begin = (currentPage - 1) * ShowAllMoviesTag.getMoviesPerPage();
            int end = ShowAllMoviesTag.getMoviesPerPage() + begin;

            List<Movie> moviesToDisplay = movieService.getAllBetween(begin, end);
            List<MovieDTO> movieDTOS = moviesToDisplay.stream().map(MovieDTO::new).collect(Collectors.toList());
            req.setSessionAttribute(Attribute.MOVIES_TO_DISPLAY, movieDTOS);
            req.setSessionAttribute(Attribute.MOVIE_COUNT, movieCount);
            if (moviesToDisplay.size() == 0) {
                req.setSessionAttribute(Attribute.INFO_MESSAGE, InfoMessage.NO_MOVIES);
            }
        } catch (ServiceException e) {
            req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
            resp = CommandResponse.redirectToPreviousPageOr(ServletDestination.MAIN, req);
        }
        return resp;
    }
}
