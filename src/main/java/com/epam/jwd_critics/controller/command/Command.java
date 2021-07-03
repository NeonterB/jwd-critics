package com.epam.jwd_critics.controller.command;

import com.epam.jwd_critics.exception.CommandException;

@FunctionalInterface
public interface Command {
    static Command of(String commandName) {
        return CommandInstance.getCommandByName(commandName);
    }

    CommandResponse execute(CommandRequest req) throws CommandException;
}
