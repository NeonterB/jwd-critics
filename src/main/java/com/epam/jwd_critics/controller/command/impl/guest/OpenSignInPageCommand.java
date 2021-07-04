package com.epam.jwd_critics.controller.command.impl.guest;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;

public class OpenSignInPageCommand implements Command {
    @Override
    public CommandResponse execute(CommandRequest req) {
        String previousPage = req.getParameter(Parameter.PREVIOUS_PAGE);
        if (previousPage != null) {
            req.setSessionAttribute(Attribute.PREVIOUS_PAGE, previousPage);
        }
        return new CommandResponse(ServletDestination.SIGN_IN, TransferType.REDIRECT);
    }
}
