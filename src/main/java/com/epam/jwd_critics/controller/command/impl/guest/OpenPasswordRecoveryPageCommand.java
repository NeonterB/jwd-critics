package com.epam.jwd_critics.controller.command.impl.guest;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.service.UserService;
import com.epam.jwd_critics.service.impl.UserServiceImpl;

public class OpenPasswordRecoveryPageCommand implements Command {
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse resp = CommandResponse.redirectToPreviousPageOr(ServletDestination.MAIN, req);
        String userIdStr = req.getParameter(Parameter.USER_ID);
        String activationKey = req.getParameter(Parameter.RECOVERY_KEY);
        if (userIdStr == null || activationKey == null) {
            throw new CommandException(ErrorMessage.MISSING_ARGUMENTS);
        }
        try {
            int userId = Integer.parseInt(userIdStr);
            userService.deleteRecoveryKey(userId, activationKey);
            req.setSessionAttribute(Attribute.USER_ID, userId);
            resp = new CommandResponse(ServletDestination.PASSWORD_RECOVERY, TransferType.REDIRECT);
        } catch (ServiceException e) {
            req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
        }
        return resp;
    }
}
