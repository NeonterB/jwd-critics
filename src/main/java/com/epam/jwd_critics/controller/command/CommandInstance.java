package com.epam.jwd_critics.controller.command;

import com.epam.jwd_critics.controller.command.impl.ShowLoginPageCommand;
import com.epam.jwd_critics.controller.command.impl.ShowMainPageCommand;

public enum CommandInstance {
    SHOW_MAIN(new ShowMainPageCommand()),
    SHOW_LOGIN(new ShowLoginPageCommand());
    private final Command command;

    CommandInstance(Command command) {
        this.command = command;
    }

    static Command commandOf(String commandName) {
        for (CommandInstance v : values()) {
            if (v.name().equalsIgnoreCase(commandName)) {
                return v.command;
            }
        }
        //todo exception?
        return SHOW_MAIN.command;
    }
}
