package com.epam.jwd_critics.controller.command;

@FunctionalInterface
public interface Command {
    static Command of(String commandName) {
        return CommandInstance.commandOf(commandName);
    }

    CommandResponse execute(CommandRequest req);
}
