package com.epam.jwd_critics.controller.command.impl.admin;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.controller.command.impl.user.UploadPictureCommand;
import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.message.SuccessMessage;
import com.epam.jwd_critics.service.CelebrityService;
import com.epam.jwd_critics.service.impl.CelebrityServiceImpl;
import com.epam.jwd_critics.validation.CelebrityValidator;
import com.epam.jwd_critics.validation.ConstraintViolation;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UpdateCelebrityCommand implements Command {
    private final CelebrityService celebrityService = CelebrityServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse resp = new CommandResponse(ServletDestination.UPDATE_CELEBRITY, TransferType.REDIRECT);
        new UploadPictureCommand().execute(req);
        String newPicture = (String) req.getAttribute(Attribute.NEW_IMAGE);
        String firstName = req.getParameter(Parameter.FIRST_NAME);
        String lastName = req.getParameter(Parameter.LAST_NAME);
        String celebrityIdStr = req.getParameter(Parameter.CELEBRITY_ID);
        if (firstName == null || lastName == null) {
            req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, ErrorMessage.EMPTY_FIELDS);
            return resp;
        }
        if (celebrityIdStr == null) {
            throw new CommandException(ErrorMessage.MISSING_ARGUMENTS);
        }

        CelebrityValidator celebrityValidator = new CelebrityValidator();
        Set<ConstraintViolation> violations = celebrityValidator.validateData(firstName, lastName);

        if (violations.isEmpty()) {
            try {
                Optional<Celebrity> celebrityToUpdate = celebrityService.getEntityById(Integer.parseInt(celebrityIdStr));
                if (celebrityToUpdate.isPresent()) {
                    celebrityToUpdate.get().setFirstName(firstName);
                    celebrityToUpdate.get().setLastName(lastName);
                    if (newPicture != null && !newPicture.equals("")) {
                        celebrityToUpdate.get().setImagePath(newPicture);
                    }
                    celebrityService.update(celebrityToUpdate.get());
                    req.setSessionAttribute(Attribute.CELEBRITY, celebrityToUpdate.get());
                    req.setSessionAttribute(Attribute.SUCCESS_NOTIFICATION, SuccessMessage.CELEBRITY_UPDATED);
                    resp.setDestination(ServletDestination.CELEBRITY_PROFILE);
                } else {
                    req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, ErrorMessage.CELEBRITY_DOES_NOT_EXIST);
                }
            } catch (ServiceException e) {
                req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
            }
        } else {
            req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList()));
        }
        return resp;
    }
}
