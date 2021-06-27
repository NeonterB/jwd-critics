package com.epam.jwd_critics.controller.command.impl.common;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;

public class ChangeLocale implements Command {
    @Override
    public CommandResponse execute(CommandRequest req) {
        String lang = req.getParameter(Parameter.LANG);
        req.setSessionAttribute(Attribute.LANG, lang);
        return CommandResponse.redirectToPreviousPageOr(ServletDestination.MAIN, req);
    }
}