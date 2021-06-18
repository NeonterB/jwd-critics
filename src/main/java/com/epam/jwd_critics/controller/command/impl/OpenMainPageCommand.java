package com.epam.jwd_critics.controller.command.impl;

import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.TransferType;

import static com.epam.jwd_critics.controller.command.ServletDestination.MAIN_PAGE;

public class OpenMainPageCommand implements Command {
    @Override
    public CommandResponse execute(CommandRequest req) {
        return new CommandResponse(MAIN_PAGE, TransferType.FORWARD);
    }
}
