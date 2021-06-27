package com.epam.jwd_critics.controller.command.impl.admin;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.controller.command.impl.common.OpenUserProfilePage;
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

public class UpdateUserStatus implements Command {
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse resp = new CommandResponse(ServletDestination.ALL_USERS, TransferType.REDIRECT);

        String userToUpdateId = req.getParameter(Parameter.USER_ID);
        String newStatusStr = req.getParameter(Parameter.NEW_STATUS);
        if (newStatusStr == null || userToUpdateId == null) {
            throw new CommandException(ErrorMessage.MISSING_ARGUMENTS);
        }
        try {
            Optional<User> userToUpdate = userService.getEntityById(Integer.parseInt(userToUpdateId));
            if (userToUpdate.isPresent()) {
                if (userToUpdate.get().getStatus().equals(Status.INACTIVE)) {
                    req.setSessionAttribute(Attribute.INFO_MESSAGE, InfoMessage.INACTIVE_USER);
                    return CommandResponse.redirectToPreviousPageOr(ServletDestination.ALL_USERS, req);
                }
                Status newStatus = Status.valueOf(newStatusStr.toUpperCase());
                userToUpdate.get().setStatus(newStatus);
                userService.update(userToUpdate.get());
                String message = null;
                switch (newStatus){
                    case BANNED:{
                        message = SuccessMessage.USER_BANNED;
                        break;
                    }
                    case ACTIVE:{
                        message = SuccessMessage.USER_UNBANNED;
                        break;
                    }
                }
                req.setSessionAttribute(Attribute.SUCCESS_NOTIFICATION, message);
            } else {
                req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, ErrorMessage.USER_DOES_NOT_EXIST);
            }
        } catch (ServiceException e) {
            req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
        }

        String page = req.getParameter(Parameter.PREVIOUS_PAGE);
        if (page != null) {
            if (page.equals(ServletDestination.USER_PROFILE.getPath())) {
                new OpenUserProfilePage().execute(req);
            } else if (page.equals(ServletDestination.ALL_USERS.getPath())) {
                new OpenAllUsersPage().execute(req);
            }
            resp.setDestination(() -> page);
        }
        return resp;
    }
}
