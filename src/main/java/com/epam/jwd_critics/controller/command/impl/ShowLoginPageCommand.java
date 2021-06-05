package com.epam.jwd_critics.controller.command.impl;

import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;

import static com.epam.jwd_critics.controller.command.ServletDestination.LOGIN_PAGE;

public class ShowLoginPageCommand implements Command {
    @Override
    public CommandResponse execute(CommandRequest req) {
        return () -> LOGIN_PAGE;
    }
}
