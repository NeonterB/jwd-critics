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
import com.epam.jwd_critics.message.SuccessMessage;
import com.epam.jwd_critics.service.UserService;
import com.epam.jwd_critics.service.impl.UserServiceImpl;

import java.util.Collections;

public class UpdatePasswordCommand implements Command {
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse resp = new CommandResponse(ServletDestination.PASSWORD_RECOVERY, TransferType.REDIRECT);
        String userIdStr = req.getParameter(Parameter.USER_ID);
        String newPassword = req.getParameter(Parameter.NEW_PASSWORD);
        String confirmNewPassword = req.getParameter(Parameter.CONFIRM_NEW_PASSWORD);
        if (newPassword == null || confirmNewPassword == null) {
            req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, Collections.singletonList(ErrorMessage.EMPTY_FIELDS));
            return resp;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, ErrorMessage.PASSWORDS_DO_NOT_MATCH);
            return resp;
        }
        try {
            userService.updatePassword(Integer.parseInt(userIdStr), newPassword.toCharArray());
            req.setSessionAttribute(Attribute.SUCCESS_NOTIFICATION, SuccessMessage.PASSWORD_WAS_UPDATED);
        } catch (ServiceException e) {
            req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
            return resp;
        }
        resp.setDestination(ServletDestination.SIGN_IN);
        return resp;
    }
}
