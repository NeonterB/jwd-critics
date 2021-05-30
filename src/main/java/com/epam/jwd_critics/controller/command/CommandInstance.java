package com.epam.jwd_critics.controller.command;

import com.epam.jwd_critics.controller.command.impl.ShowMainPageCommand;

public enum CommandInstance {
    SHOW_MAIN(new ShowMainPageCommand());
    private final Command command;

    CommandInstance(Command command) {
        this.command = command;
    }

    static Command commandOf(String commandName) {
        for (CommandInstance v : values()) {
            if (v.name().equals(commandName)) {
                return v.command;
            }
        }
        //todo exception?
        return null;
    }
}
