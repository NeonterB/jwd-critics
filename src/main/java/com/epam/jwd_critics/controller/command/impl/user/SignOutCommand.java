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
        CommandResponse resp = CommandResponse.redirectToPreviousPageOr(ServletDestination.MAIN, req);
        if (resp.getDestination().getPath().contains(ServletDestination.USER_URL) ||
                resp.getDestination().getPath().contains(ServletDestination.ADMIN_URL)) {
            resp.setDestination(ServletDestination.MAIN);
        }
        return resp;
    }
}
