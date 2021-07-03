package com.epam.jwd_critics.controller.command.impl.admin;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.exception.CommandException;

public class OpenCreateCelebrityPageCommand implements Command {
    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        req.removeSessionAttribute(Attribute.CELEBRITY);
        return new CommandResponse(ServletDestination.UPDATE_CELEBRITY, TransferType.REDIRECT);
    }
}
