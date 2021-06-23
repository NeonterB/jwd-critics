package com.epam.jwd_critics.controller.command.impl.admin;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.controller.command.impl.common.OpenUserProfilePageCommand;
import com.epam.jwd_critics.entity.Status;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.message.InfoMessage;
import com.epam.jwd_critics.message.SuccessMessage;
import com.epam.jwd_critics.service.UserService;
import com.epam.jwd_critics.service.impl.UserServiceImpl;

import java.util.Optional;

public class UpdateUserStatusCommand implements Command {
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse resp = new CommandResponse(ServletDestination.ALL_USERS, TransferType.REDIRECT);

        String userToUpdateId = req.getParameter(Parameter.USER_ID);
        String newStatus = req.getParameter(Parameter.NEW_STATUS);
        if (newStatus == null || userToUpdateId == null) {
            throw new CommandException(ErrorMessage.MISSING_ARGUMENTS);
        }
        try {
            Optional<User> userToUpdate = userService.getEntityById(Integer.parseInt(userToUpdateId));
            if (userToUpdate.isPresent()) {
                if (userToUpdate.get().getStatus().equals(Status.INACTIVE)) {
                    req.setSessionAttribute(Attribute.INFO_MESSAGE, InfoMessage.INACTIVE_USER);
                    return CommandResponse.redirectToPreviousPageOr(ServletDestination.ALL_USERS, req);
                }
                userToUpdate.get().setStatus(Status.valueOf(newStatus.toUpperCase()));
                userService.update(userToUpdate.get());
                req.setSessionAttribute(Attribute.SUCCESS_NOTIFICATION, SuccessMessage.USER_BANNED);
            } else {
                req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, ErrorMessage.USER_DOES_NOT_EXIST);
            }
        } catch (ServiceException e) {
            req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
        }

        String page = req.getParameter(Parameter.CURRENT_PAGE);
        if (page != null) {
            if (page.equals(ServletDestination.USER_PROFILE.getPath())) {
                new OpenUserProfilePageCommand().execute(req);
            } else if (page.equals(ServletDestination.ALL_USERS.getPath())) {
                new OpenAllUsersPageCommand().execute(req);
            }
            resp.setDestination(() -> page);
        }
        return resp;
    }
}
