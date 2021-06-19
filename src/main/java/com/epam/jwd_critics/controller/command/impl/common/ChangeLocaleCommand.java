package com.epam.jwd_critics.controller.command.impl.common;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.TransferType;

public class ChangeLocaleCommand implements Command {
    @Override
    public CommandResponse execute(CommandRequest req) {
        String page = req.getParameter(Parameter.CURRENT_PAGE);
        String lang = req.getParameter(Parameter.LANG);
        req.setSessionAttribute(Attribute.LANG, lang);
        return new CommandResponse(() -> page, TransferType.FORWARD);
    }
}
