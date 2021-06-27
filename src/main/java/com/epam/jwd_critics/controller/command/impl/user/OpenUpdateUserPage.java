package com.epam.jwd_critics.controller.command.impl.user;

import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.exception.CommandException;

public class OpenUpdateUserPage implements Command {
    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        return new CommandResponse(ServletDestination.UPDATE_USER, TransferType.REDIRECT);
    }
}
