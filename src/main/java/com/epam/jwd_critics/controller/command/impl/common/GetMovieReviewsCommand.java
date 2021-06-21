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
import com.epam.jwd_critics.model.dto.MovieDTO;
import com.epam.jwd_critics.model.dto.MovieReviewDTO;
import com.epam.jwd_critics.model.entity.MovieReview;
import com.epam.jwd_critics.model.entity.User;
import com.epam.jwd_critics.model.service.MovieReviewService;
import com.epam.jwd_critics.model.service.UserService;
import com.epam.jwd_critics.model.service.impl.MovieReviewServiceImpl;
import com.epam.jwd_critics.model.service.impl.UserServiceImpl;
import com.epam.jwd_critics.tag.ShowReviewsTag;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class GetMovieReviewsCommand implements Command {
    private final MovieReviewService movieReviewService = MovieReviewServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse commandResult = new CommandResponse(ServletDestination.MOVIE, TransferType.FORWARD);
        Integer movieId = ((MovieDTO) req.getSessionAttribute(Attribute.MOVIE)).getId();
        if (movieId == null) {
            req.setSessionAttribute(Attribute.GLOBAL_ERROR, "Empty movie id");
            commandResult.setDestination(ServletDestination.MAIN);
            commandResult.setTransferType(TransferType.REDIRECT);
        } else {
            Integer currentPage = (Integer) req.getAttribute(Attribute.REVIEWS_CURRENT_PAGE);
            String newPageStr = req.getParameter(Parameter.NEW_REVIEW_PAGE);
            if (newPageStr != null) {
                currentPage = Integer.parseInt(newPageStr);
            } else if (currentPage == null) {
                currentPage = 1;
            }
            req.setAttribute(Attribute.REVIEWS_CURRENT_PAGE, currentPage);
            int begin = (currentPage - 1) * ShowReviewsTag.REVIEWS_PER_PAGE;
            int end = ShowReviewsTag.REVIEWS_PER_PAGE + begin;
            try {
                List<MovieReview> reviews = movieReviewService.getMovieReviewsByMovieId(movieId, begin, end);
                List<MovieReviewDTO> reviewsToDisplay = new LinkedList<>();
                for (MovieReview review : reviews) {
                    MovieReviewDTO reviewDTO = new MovieReviewDTO(review);
                    Optional<User> user = userService.getEntityById(reviewDTO.getUserId());
                    if (user.isPresent()) {
                        reviewDTO.setUserImagePath(user.get().getImagePath());
                        reviewDTO.setUserName(user.get().getFirstName());
                        reviewsToDisplay.add(reviewDTO);
                    }
                }
                req.setAttribute(Attribute.REVIEWS_TO_DISPLAY, reviewsToDisplay);
                int reviewCount = movieReviewService.getCountByMovieId(movieId);
                req.setAttribute(Attribute.REVIEW_COUNT, reviewCount);
                if (reviewsToDisplay.size() == 0) {
                    req.setSessionAttribute(Attribute.REPORT_MESSAGE, "No reviews here yet");
                }
            } catch (ServiceException e) {
                req.setSessionAttribute(Attribute.SERVICE_ERROR, e.getMessage());
                commandResult.setDestination(ServletDestination.MAIN);
                commandResult.setTransferType(TransferType.REDIRECT);
            }
        }
        return commandResult;
    }
}
