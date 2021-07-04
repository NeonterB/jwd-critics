package com.epam.jwd_critics.controller.command.impl.admin;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.Position;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.message.SuccessMessage;
import com.epam.jwd_critics.service.CelebrityService;
import com.epam.jwd_critics.service.MovieService;
import com.epam.jwd_critics.service.impl.CelebrityServiceImpl;
import com.epam.jwd_critics.service.impl.MovieServiceImpl;
import com.epam.jwd_critics.validation.CelebrityValidator;
import com.epam.jwd_critics.validation.ConstraintViolation;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AssignCelebrityOnPositionCommand implements Command {
    private final MovieService movieService = MovieServiceImpl.getInstance();
    private final CelebrityService celebrityService = CelebrityServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse resp = new CommandResponse(ServletDestination.UPDATE_MOVIE, TransferType.REDIRECT);
        String celebrityFirstName = req.getParameter(Parameter.FIRST_NAME);
        String celebrityLastName = req.getParameter(Parameter.LAST_NAME);
        String movieIdStr = req.getParameter(Parameter.MOVIE_ID);
        String[] positionIds = req.getParameters(Parameter.CELEBRITY_POSITIONS);
        if (celebrityFirstName == null || celebrityLastName == null || positionIds == null) {
            req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, ErrorMessage.EMPTY_FIELDS);
            return resp;
        }
        if (movieIdStr == null) {
            throw new CommandException(ErrorMessage.MISSING_ARGUMENTS);
        }
        CelebrityValidator validator = new CelebrityValidator();
        Set<ConstraintViolation> violations = validator.validateData(celebrityFirstName, celebrityLastName, positionIds);
        if (violations.isEmpty()) {
            try {
                Optional<Movie> movie = movieService.getEntityById(Integer.parseInt(movieIdStr));
                if (!movie.isPresent()) {
                    req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, ErrorMessage.MOVIE_DOES_NOT_EXIST);
                    return resp;
                }
                Optional<Celebrity> celebrity = celebrityService.getEntityByFullName(celebrityFirstName, celebrityLastName);
                if (celebrity.isPresent()) {
                    Map<Movie, List<Position>> jobs = celebrity.get().getJobs();
                    for (String positionId : positionIds) {
                        Position position = Position.resolvePositionById(Integer.parseInt(positionId)).get();
                        List<Position> positions = jobs.get(movie.get());
                        if (positions == null || !positions.contains(position)) {
                            movieService.assignCelebrityOnPosition(
                                    Integer.parseInt(movieIdStr),
                                    celebrity.get().getId(),
                                    position
                            );
                            req.setSessionAttribute(Attribute.SUCCESS_NOTIFICATION, SuccessMessage.CELEBRITY_ASSIGNED_ON_POSITION);
                            new OpenUpdateMoviePageCommand().execute(req);
                        }
                    }
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