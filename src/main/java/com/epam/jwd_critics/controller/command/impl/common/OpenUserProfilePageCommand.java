package com.epam.jwd_critics.controller.command.impl.common;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.dto.MovieReviewDTO;
import com.epam.jwd_critics.dto.UserDTO;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.service.MovieReviewService;
import com.epam.jwd_critics.service.MovieService;
import com.epam.jwd_critics.service.UserService;
import com.epam.jwd_critics.service.impl.MovieReviewServiceImpl;
import com.epam.jwd_critics.service.impl.MovieServiceImpl;
import com.epam.jwd_critics.service.impl.UserServiceImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class OpenUserProfilePageCommand implements Command {
    private final MovieReviewService reviewService = MovieReviewServiceImpl.getInstance();
    private final MovieService movieService = MovieServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse resp = new CommandResponse(ServletDestination.USER_PROFILE, TransferType.FORWARD);
        int userId;
        String userIdStr = req.getParameter(Parameter.USER_ID);
        if (userIdStr == null) {
            UserDTO userProfile = (UserDTO) req.getSessionAttribute(Attribute.USER_PROFILE);
            if (userProfile == null) {
                throw new CommandException(ErrorMessage.MISSING_ARGUMENTS);
            }
            userId = userProfile.getId();
        } else {
            userId = Integer.parseInt(userIdStr);
        }
        try {
            Optional<User> user = userService.getEntityById(userId);
            if (user.isPresent()) {
                int reviewCount = reviewService.getCountByUserId(userId);
                List<MovieReview> reviews = reviewService.getMovieReviewsByUserId(userId, 0, reviewCount);
                List<MovieReviewDTO> reviewDTOS = new LinkedList<>();
                for (MovieReview review : reviews) {
                    Optional<Movie> movieOfReview = movieService.getEntityById(review.getUserId());
                    movieOfReview.ifPresent(value -> reviewDTOS.add(new MovieReviewDTO(review, value)));
                }
                UserDTO userDTO = new UserDTO(user.get());
                req.setSessionAttribute(Attribute.USER_PROFILE, userDTO);
                req.setSessionAttribute(Attribute.REVIEWS_ON_USER_PROFILE_PAGE, reviewDTOS);
            } else {
                req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, ErrorMessage.USER_DOES_NOT_EXIST);
                resp = CommandResponse.redirectToPreviousPageOr(ServletDestination.MAIN, req);
            }
        } catch (ServiceException e) {
            req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
            resp = CommandResponse.redirectToPreviousPageOr(ServletDestination.MAIN, req);
        }
        return resp;
    }
}
