package com.epam.jwd_critics.controller.command.impl.common;

import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;

public class OpenMainPageCommand implements Command {
    @Override
    public CommandResponse execute(CommandRequest req) {
        return new CommandResponse(ServletDestination.MAIN, TransferType.FORWARD);
    }
}
