package com.epam.jwd_critics.controller.command.impl.common;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.TransferType;

import static com.epam.jwd_critics.controller.command.ServletDestination.SIGN_IN;

public class OpenSignInPageCommand implements Command {
    @Override
    public CommandResponse execute(CommandRequest req) {
        String currentPage = req.getParameter(Parameter.CURRENT_PAGE);
        if (currentPage != null) {
            req.setAttribute(Attribute.CURRENT_PAGE, currentPage);
        }
        return new CommandResponse(SIGN_IN, TransferType.FORWARD);
    }
}
