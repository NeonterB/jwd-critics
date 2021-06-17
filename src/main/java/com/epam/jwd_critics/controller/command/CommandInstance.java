package com.epam.jwd_critics.controller.command;

import com.epam.jwd_critics.controller.command.impl.SignInCommand;
import com.epam.jwd_critics.controller.command.impl.RegisterCommand;
import com.epam.jwd_critics.controller.command.impl.ShowSignInPageCommand;
import com.epam.jwd_critics.controller.command.impl.ShowMainPageCommand;
import com.epam.jwd_critics.controller.command.impl.SignOutCommand;

public enum CommandInstance {
    SHOW_MAIN(new ShowMainPageCommand()),
    SHOW_LOGIN(new ShowSignInPageCommand()),
    SIGN_IN(new SignInCommand()),
    REGISTER(new RegisterCommand()),
    SIGN_OUT(new SignOutCommand());
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
