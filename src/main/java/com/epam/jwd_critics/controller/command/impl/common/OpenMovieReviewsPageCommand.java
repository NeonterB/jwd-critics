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
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.service.MovieReviewService;
import com.epam.jwd_critics.service.UserService;
import com.epam.jwd_critics.service.impl.MovieReviewServiceImpl;
import com.epam.jwd_critics.service.impl.UserServiceImpl;
import com.epam.jwd_critics.tag.ShowReviewsTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class OpenMovieReviewsPageCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(OpenAllMoviesPageCommand.class);
    private final MovieReviewService reviewService = MovieReviewServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) {
        CommandResponse commandResult = new CommandResponse(ServletDestination.REVIEWS, TransferType.FORWARD);
        MovieDTO movie = (MovieDTO) req.getSessionAttribute(Attribute.MOVIE);
        if (movie == null) {
            req.setSessionAttribute(Attribute.GLOBAL_ERROR, "Empty movie");
            commandResult.setDestination(ServletDestination.MAIN);
            commandResult.setTransferType(TransferType.REDIRECT);
        } else {
            try {
                Integer currentPage = (Integer) req.getSessionAttribute(Attribute.REVIEWS_CURRENT_PAGE);
                String newPageStr = req.getParameter(Parameter.NEW_REVIEW_PAGE);
                int reviewCount = reviewService.getCountByMovieId(movie.getId());
                req.setSessionAttribute(Attribute.REVIEW_COUNT, reviewCount);
                if (newPageStr != null) {
                    currentPage = Integer.parseInt(newPageStr);
                } else if (currentPage == null) {
                    currentPage = 1;
                } else if (currentPage > 0 && (currentPage - 1) * ShowReviewsTag.REVIEWS_PER_PAGE >= reviewCount) {
                    currentPage--;
                }
                req.setSessionAttribute(Attribute.REVIEWS_CURRENT_PAGE, currentPage);
                int begin = (currentPage - 1) * ShowReviewsTag.REVIEWS_PER_PAGE;
                int end = ShowReviewsTag.REVIEWS_PER_PAGE + begin;

                List<MovieReview> reviews = reviewService.getMovieReviewsByMovieId(movie.getId(), begin, end);
                List<MovieReviewDTO> reviewDTOS = new LinkedList<>();
                for (MovieReview review : reviews) {
                    MovieReviewDTO reviewDTO = new MovieReviewDTO(review);
                    Optional<User> user = userService.getEntityById(reviewDTO.getUserId());
                    if (user.isPresent()) {
                        reviewDTO.setUserImagePath(user.get().getImagePath());
                        reviewDTO.setUserName(user.get().getFirstName());
                        reviewDTOS.add(reviewDTO);
                    }
                }
                req.setSessionAttribute(Attribute.REVIEWS_TO_DISPLAY, reviewDTOS);
                if (reviewDTOS.size() == 0) {
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
