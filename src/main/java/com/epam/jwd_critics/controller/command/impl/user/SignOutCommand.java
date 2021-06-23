package com.epam.jwd_critics.controller.command.impl.user;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.ServletDestination;

public class SignOutCommand implements Command {
    @Override
    public CommandResponse execute(CommandRequest req) {
        req.removeSessionAttribute(Attribute.USER);
        req.removeSessionAttribute(Attribute.USER_REVIEW);
        return CommandResponse.redirectToPreviousPageOr(ServletDestination.MAIN, req);
    }
}
