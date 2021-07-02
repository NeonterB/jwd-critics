package com.epam.jwd_critics.controller.command.impl.admin;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.controller.command.impl.user.UploadPictureCommand;
import com.epam.jwd_critics.entity.AgeRestriction;
import com.epam.jwd_critics.entity.Country;
import com.epam.jwd_critics.entity.Genre;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.message.SuccessMessage;
import com.epam.jwd_critics.service.MovieService;
import com.epam.jwd_critics.service.impl.MovieServiceImpl;
import com.epam.jwd_critics.validation.ConstraintViolation;
import com.epam.jwd_critics.validation.MovieValidator;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateMovieCommand implements Command {
    private final MovieService movieService = MovieServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        new UploadPictureCommand().execute(req);
        String newPicture = (String) req.getAttribute(Attribute.NEW_IMAGE);
        String name = req.getParameter(Parameter.MOVIE_NAME);
        String releaseDate = req.getParameter(Parameter.MOVIE_RELEASE_DATE);
        String runtime = req.getParameter(Parameter.MOVIE_RUNTIME);
        String country = req.getParameter(Parameter.MOVIE_COUNTRY);
        String ageRestriction = req.getParameter(Parameter.MOVIE_AGE_RESTRICTION);
        String summary = req.getParameter(Parameter.MOVIE_SUMMARY);
        String[] genreIds = req.getParameters(Parameter.GENRES);
        if (name == null || releaseDate == null || runtime == null || country == null || ageRestriction == null || summary == null || genreIds == null) {
            req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, ErrorMessage.EMPTY_FIELDS);
            return new CommandResponse(ServletDestination.UPDATE_USER, TransferType.REDIRECT);
        }

        MovieValidator movieValidator = new MovieValidator();
        Set<ConstraintViolation> violations = movieValidator.validateMovieData(name, releaseDate, runtime, country, ageRestriction, summary, genreIds);

        if (violations.isEmpty()) {
            try {
                String[] split = runtime.split(":");
                Duration duration = Duration.ofHours(Long.parseLong(split[0])).plusMinutes(Long.parseLong(split[1]));

                List<Genre> genres = Arrays.stream(genreIds)
                        .map(id -> Genre.resolveGenreById(Integer.parseInt(id)).get())
                        .collect(Collectors.toList());

                Movie movie = Movie.newBuilder().setName(name)
                        .setReleaseDate(LocalDate.parse(releaseDate))
                        .setRuntime(duration)
                        .setCountry(Country.valueOf(country.toUpperCase()))
                        .setAgeRestriction(AgeRestriction.valueOf(ageRestriction.toUpperCase().replace("-", "_")))
                        .setSummary(summary)
                        .setImagePath(newPicture)
                        .setGenres(genres)
                        .build();
                movieService.create(movie);

                req.setSessionAttribute(Attribute.MOVIE, movie);
                req.setSessionAttribute(Attribute.SUCCESS_NOTIFICATION, SuccessMessage.MOVIE_CREATED);
            } catch (ServiceException e) {
                req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
            }
        } else {
            req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList()));
        }
        return new CommandResponse(ServletDestination.UPDATE_MOVIE, TransferType.REDIRECT);
    }
}
