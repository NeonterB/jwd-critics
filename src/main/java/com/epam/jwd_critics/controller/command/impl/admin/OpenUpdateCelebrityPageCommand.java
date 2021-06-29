package com.epam.jwd_critics.controller.command.impl.admin;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.dto.CelebrityDTO;
import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.service.CelebrityService;
import com.epam.jwd_critics.service.impl.CelebrityServiceImpl;

import java.util.Optional;

public class OpenUpdateCelebrityPageCommand implements Command {
    private final CelebrityService celebrityService = CelebrityServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse resp = new CommandResponse(ServletDestination.UPDATE_CELEBRITY, TransferType.REDIRECT);
        String celebrityIdStr = req.getParameter(Parameter.CELEBRITY_ID);
        if (celebrityIdStr == null) {
            throw new CommandException(ErrorMessage.MISSING_ARGUMENTS);
        }
        try {
            Optional<Celebrity> celebrity = celebrityService.getEntityById(Integer.parseInt(celebrityIdStr));
            if (celebrity.isPresent()) {
                req.setSessionAttribute(Attribute.CELEBRITY, new CelebrityDTO(celebrity.get()));
            } else {
                req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, ErrorMessage.CELEBRITY_DOES_NOT_EXIST);
                return CommandResponse.redirectToPreviousPageOr(ServletDestination.MAIN, req);
            }
        } catch (ServiceException e) {
            req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
            return CommandResponse.redirectToPreviousPageOr(ServletDestination.MAIN, req);
        }
        return resp;
    }
}
