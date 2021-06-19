package com.epam.jwd_critics.controller.command.impl.common;

import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.TransferType;

import static com.epam.jwd_critics.controller.command.ServletDestination.MAIN;

public class OpenMainPageCommand implements Command {
    @Override
    public CommandResponse execute(CommandRequest req) {
        return new CommandResponse(MAIN, TransferType.FORWARD);
    }
}
