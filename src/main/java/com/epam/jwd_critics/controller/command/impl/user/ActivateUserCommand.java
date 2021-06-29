package com.epam.jwd_critics.controller.command.impl.user;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.dto.UserDTO;
import com.epam.jwd_critics.entity.Status;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.message.SuccessMessage;
import com.epam.jwd_critics.service.UserService;
import com.epam.jwd_critics.service.impl.UserServiceImpl;

import java.util.Optional;

public class ActivateUserCommand implements Command {
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        String userIdStr = req.getParameter(Parameter.USER_ID);
        String activationKey = req.getParameter(Parameter.ACTIVATION_KEY);
        if (userIdStr == null || activationKey == null) {
            throw new CommandException(ErrorMessage.MISSING_ARGUMENTS);
        }
        try {
            int userId = Integer.parseInt(userIdStr);
            Optional<User> user = userService.getEntityById(userId);
            if (user.isPresent()) {
                userService.deleteActivationKey(userId, activationKey);
                if (user.get().getStatus().equals(Status.BANNED)) {
                    req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, ErrorMessage.USER_IS_BANNED);
                } else {
                    user.get().setStatus(Status.ACTIVE);
                    userService.update(user.get());
                    UserDTO sessionUser = (UserDTO) req.getSessionAttribute(Attribute.USER);
                    if (sessionUser.getId() == userId) {
                        sessionUser.setStatus(Status.ACTIVE);
                        req.setSessionAttribute(Attribute.USER, sessionUser);
                    }
                    req.setSessionAttribute(Attribute.SUCCESS_NOTIFICATION, SuccessMessage.USER_ACTIVATED);
                }
            } else {
                req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, ErrorMessage.USER_DOES_NOT_EXIST);
            }
        } catch (ServiceException e) {
            req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
        }

        return new CommandResponse(ServletDestination.MAIN, TransferType.REDIRECT);
    }
}
