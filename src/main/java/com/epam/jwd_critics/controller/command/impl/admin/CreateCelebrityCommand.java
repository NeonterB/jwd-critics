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

import java.util.Set;
import java.util.stream.Collectors;

public class CreateCelebrityCommand implements Command {
    private final CelebrityService celebrityService = CelebrityServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        new UploadPictureCommand().execute(req);
        String newPicture = (String) req.getAttribute(Attribute.NEW_IMAGE);
        String firstName = req.getParameter(Parameter.FIRST_NAME);
        String lastName = req.getParameter(Parameter.LAST_NAME);
        if (firstName == null || lastName == null) {
            req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, ErrorMessage.EMPTY_FIELDS);
            return new CommandResponse(ServletDestination.UPDATE_USER, TransferType.REDIRECT);
        }

        CelebrityValidator celebrityValidator = new CelebrityValidator();
        Set<ConstraintViolation> violations = celebrityValidator.validateData(firstName, lastName);

        if (violations.isEmpty()) {
            try {
                Celebrity celebrity = Celebrity.newBuilder()
                        .setFirstName(firstName)
                        .setLastName(lastName)
                        .setImagePath(newPicture)
                        .build();
                celebrity = celebrityService.create(celebrity);

                req.setSessionAttribute(Attribute.CELEBRITY, celebrity);
                req.setSessionAttribute(Attribute.SUCCESS_NOTIFICATION, SuccessMessage.CELEBRITY_CREATED);
            } catch (ServiceException e) {
                req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
            }
        } else {
            req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList()));
        }
        return new CommandResponse(ServletDestination.UPDATE_CELEBRITY, TransferType.REDIRECT);
    }
}
