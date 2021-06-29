package com.epam.jwd_critics.controller.command.impl.guest;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.message.InfoMessage;
import com.epam.jwd_critics.service.UserService;
import com.epam.jwd_critics.service.impl.UserServiceImpl;

import java.util.Optional;
import java.util.UUID;

public class SendRecoveryMailCommand implements Command {
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        String userEmail = req.getParameter(Parameter.EMAIL);
        if (userEmail == null) {
            throw new CommandException(ErrorMessage.MISSING_ARGUMENTS);
        }
        try {
            Optional<User> user = userService.getEntityByEmail(userEmail);
            if (user.isPresent()) {
                Optional<String> currentKey = userService.getRecoveryKey(user.get().getId());
                String key;
                if (!currentKey.isPresent()) {
                    key = UUID.randomUUID().toString();
                    userService.createRecoveryKey(user.get().getId(), key);
                } else {
                    key = currentKey.get();
                }
                String lang = (String) req.getSessionAttribute(Attribute.LANG);
                userService.buildAndSendRecoveryMail(user.get(), key, lang);
                req.setSessionAttribute(Attribute.INFO_MESSAGE, InfoMessage.RECOVERY_MAIL);
            } else {
                req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, ErrorMessage.USER_DOES_NOT_EXIST);
            }
        } catch (ServiceException e) {
            req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
        }

        return new CommandResponse(ServletDestination.MAIN, TransferType.REDIRECT);
    }
}
