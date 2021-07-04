package com.epam.jwd_critics.controller.command.impl.common;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.service.CelebrityService;
import com.epam.jwd_critics.service.impl.CelebrityServiceImpl;

import java.util.Optional;

public class OpenCelebrityProfilePageCommand implements Command {
    private final CelebrityService celebrityService = CelebrityServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse resp = CommandResponse.redirectToPreviousPageOr(ServletDestination.MAIN, req);
        String celebrityIdStr = req.getParameter(Parameter.CELEBRITY_ID);
        if (celebrityIdStr == null) {
            throw new CommandException(ErrorMessage.MISSING_ARGUMENTS);
        }
        int celebrityId = Integer.parseInt(celebrityIdStr);
        try {
            Optional<Celebrity> celebrity = celebrityService.getEntityById(celebrityId);
            if (celebrity.isPresent()) {
                req.setSessionAttribute(Attribute.CELEBRITY, celebrity.get());
                resp = new CommandResponse(ServletDestination.CELEBRITY_PROFILE, TransferType.FORWARD);
            } else {
                req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, ErrorMessage.CELEBRITY_DOES_NOT_EXIST);
            }
        } catch (ServiceException e) {
            req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
        }
        return resp;
    }
}
