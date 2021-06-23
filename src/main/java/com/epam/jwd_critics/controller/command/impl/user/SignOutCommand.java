package com.epam.jwd_critics.controller.command.impl.user;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;

import javax.servlet.http.HttpSession;

public class SignOutCommand implements Command {
    @Override
    public CommandResponse execute(CommandRequest req) {
        req.removeSessionAttribute(Attribute.USER);
        req.removeSessionAttribute(Attribute.USER_REVIEW);
        return CommandResponse.redirectToMainOrPreviousPage(req);
    }
}
