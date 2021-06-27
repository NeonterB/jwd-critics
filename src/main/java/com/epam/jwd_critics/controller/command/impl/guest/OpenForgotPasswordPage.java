package com.epam.jwd_critics.controller.command.impl.guest;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.exception.CommandException;

public class OpenForgotPasswordPage implements Command {
    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        req.removeSessionAttribute(Attribute.PREVIOUS_PAGE);
        return new CommandResponse(ServletDestination.FORGOT_PASSWORD, TransferType.FORWARD);
    }
}
