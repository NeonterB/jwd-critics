package com.epam.jwd_critics.controller.command.impl.admin;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.entity.Position;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.message.SuccessMessage;
import com.epam.jwd_critics.service.MovieService;
import com.epam.jwd_critics.service.impl.MovieServiceImpl;
import com.epam.jwd_critics.validation.CelebrityValidator;
import com.epam.jwd_critics.validation.ConstraintViolation;

import java.util.Optional;

public class RemoveCelebrityFromPosition implements Command {
    private final MovieService movieService = MovieServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        String celebrityIdStr = req.getParameter(Parameter.CELEBRITY_ID);
        String movieIdStr = req.getParameter(Parameter.MOVIE_ID);
        String positionIdStr = req.getParameter(Parameter.POSITION_ID);
        if (celebrityIdStr == null || movieIdStr == null || positionIdStr == null) {
            req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, ErrorMessage.EMPTY_FIELDS);
            return new CommandResponse(ServletDestination.UPDATE_MOVIE, TransferType.REDIRECT);
        }
        CelebrityValidator validator = new CelebrityValidator();
        Optional<ConstraintViolation> constraintViolation = validator.validatePositionId(positionIdStr);
        if (!constraintViolation.isPresent()) {
            try {
                movieService.removeCelebrityFromPosition(
                        Integer.parseInt(movieIdStr),
                        Integer.parseInt(celebrityIdStr),
                        Position.resolvePositionById(Integer.parseInt(positionIdStr)).get()
                );
                req.setSessionAttribute(Attribute.SUCCESS_NOTIFICATION, SuccessMessage.CELEBRITY_ASSIGNED_ON_POSITION);
            } catch (ServiceException e) {
                req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
            }
        } else {
            req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, constraintViolation.get().getMessage());
        }

        return new OpenUpdateMoviePageCommand().execute(req);
    }
}
