package com.epam.jwd_critics.controller.command.impl.admin;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.controller.command.impl.common.OpenAllCelebritiesPageCommand;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.message.SuccessMessage;
import com.epam.jwd_critics.service.CelebrityService;
import com.epam.jwd_critics.service.impl.CelebrityServiceImpl;

public class DeleteCelebrityCommand implements Command {
    private final CelebrityService celebrityService = CelebrityServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse resp = new CommandResponse(ServletDestination.CELEBRITY_PROFILE, TransferType.REDIRECT);
        String celebrityId = req.getParameter(Parameter.CELEBRITY_ID);
        if (celebrityId == null) {
            throw new CommandException(ErrorMessage.MISSING_ARGUMENTS);
        }
        try {
            celebrityService.delete(Integer.parseInt(celebrityId));
            req.removeSessionAttribute(Attribute.CELEBRITY);
            req.setSessionAttribute(Attribute.SUCCESS_NOTIFICATION, SuccessMessage.CELEBRITY_DELETED);
            new OpenAllCelebritiesPageCommand().execute(req);
            resp.setDestination(ServletDestination.ALL_CELEBRITIES);
        } catch (ServiceException e) {
            req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
        }
        return resp;
    }
}
