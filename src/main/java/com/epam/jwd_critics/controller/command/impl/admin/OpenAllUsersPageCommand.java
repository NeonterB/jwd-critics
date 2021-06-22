package com.epam.jwd_critics.controller.command.impl.admin;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.dto.UserDTO;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.service.MovieReviewService;
import com.epam.jwd_critics.service.UserService;
import com.epam.jwd_critics.service.impl.MovieReviewServiceImpl;
import com.epam.jwd_critics.service.impl.UserServiceImpl;
import com.epam.jwd_critics.tag.ShowAllUsersTag;

import java.util.LinkedList;
import java.util.List;

public class OpenAllUsersPageCommand implements Command {
    private final UserService userService = UserServiceImpl.getInstance();
    private final MovieReviewService reviewService = MovieReviewServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse commandResult = new CommandResponse(ServletDestination.ALL_USERS, TransferType.FORWARD);

        Integer currentPage = (Integer) req.getSessionAttribute(Attribute.ALL_USERS_CURRENT_PAGE);
        String newPageStr = req.getParameter(Parameter.NEW_USERS_PAGE);
        if (newPageStr != null) {
            currentPage = Integer.valueOf(newPageStr);
        } else if (currentPage == null) {
            currentPage = 1;
        }
        req.setSessionAttribute(Attribute.ALL_USERS_CURRENT_PAGE, currentPage);
        int begin = (currentPage - 1) * ShowAllUsersTag.USERS_PER_PAGE;
        int end = ShowAllUsersTag.USERS_PER_PAGE + begin;
        try {
            List<User> users = userService.getAllBetween(begin, end);
            List<UserDTO> userDTOS = new LinkedList<>();
            for (User user : users) {
                UserDTO userDTO = new UserDTO(user);
                int reviewCount = reviewService.getCountByUserId(user.getId());
                userDTO.setReviews(reviewService.getMovieReviewsByUserId(user.getId(), 0, reviewCount));
                userDTOS.add(userDTO);
            }
            req.setSessionAttribute(Attribute.USERS_TO_DISPLAY, userDTOS);
            int userCount = userService.getCount();
            req.setSessionAttribute(Attribute.USER_COUNT, userCount);
            if (userDTOS.size() == 0) {
                req.setSessionAttribute(Attribute.REPORT_MESSAGE, "No users here yet");
            }
        } catch (ServiceException e) {
            req.setSessionAttribute(Attribute.SERVICE_ERROR, e.getMessage());
            commandResult.setDestination(ServletDestination.MAIN);
        }
        return commandResult;
    }
}
