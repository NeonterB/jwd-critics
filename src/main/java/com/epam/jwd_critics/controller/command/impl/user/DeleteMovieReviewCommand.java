package com.epam.jwd_critics.controller.command.impl.user;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.controller.command.impl.common.OpenMoviePageCommand;
import com.epam.jwd_critics.controller.command.impl.common.OpenMovieReviewsPageCommand;
import com.epam.jwd_critics.dto.UserDTO;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.service.MovieReviewService;
import com.epam.jwd_critics.service.impl.MovieReviewServiceImpl;

public class DeleteMovieReviewCommand implements Command {
    private final MovieReviewService reviewService = MovieReviewServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) {
        CommandResponse resp = new CommandResponse(ServletDestination.MOVIE, TransferType.REDIRECT);
        String movieReviewIdStr = req.getParameter(Parameter.MOVIE_REVIEW_ID);
        UserDTO user = (UserDTO) req.getSessionAttribute(Attribute.USER);
        if (user == null) {
            req.setSessionAttribute(Attribute.GLOBAL_ERROR, "User is empty");
        } else if (movieReviewIdStr == null) {
            req.setSessionAttribute(Attribute.VALIDATION_ERRORS, "Empty review id");
        } else {
            try {
                reviewService.delete(Integer.parseInt(movieReviewIdStr));
                req.removeSessionAttribute(Attribute.USER_REVIEW);
                req.setSessionAttribute(Attribute.SUCCESS_NOTIFICATION, "Review deleted");
            } catch (ServiceException e) {
                req.setSessionAttribute(Attribute.SERVICE_ERROR, e.getMessage());
            }
        }
        String page = (String) req.getSessionAttribute(Attribute.CURRENT_PAGE);
        if (page != null) {
            if (page.equals(ServletDestination.MOVIE.getPath())){
                new OpenMoviePageCommand().execute(req);
            } else if (page.equals(ServletDestination.REVIEWS.getPath())){
                new OpenMovieReviewsPageCommand().execute(req);
            }
            resp.setDestination(() -> page);
        }
        return resp;
    }
}
