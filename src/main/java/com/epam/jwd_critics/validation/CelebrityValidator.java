package com.epam.jwd_critics.validation;

import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.entity.Position;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CelebrityValidator {
    private static final String FIRST_NAME_REGEX = "^[A-Z][a-z]{1,14}";
    private static final String LAST_NAME_REGEX = "^([A-Z][a-z ,.'-]+)+$";

    private static final String FIRST_NAME_MESSAGE = "First name can contain letters, must start with an uppercase letter";
    private static final String LAST_NAME_MESSAGE = "FLast names can contain letters and special characters \"., '-\", must start with an uppercase letter";
    private static final String POSITION_MESSAGE = "PositionId must be and integer";
    private static final String POSITION_DOES_NOT_EXIST_MESSAGE = "Position with id %s does not exist";

    public Optional<ConstraintViolation> validateFirstName(String name) {
        if (name.matches(FIRST_NAME_REGEX)) {
            return Optional.empty();
        } else {
            return Optional.of(new ConstraintViolation(Parameter.FIRST_NAME, FIRST_NAME_MESSAGE));
        }
    }

    public Optional<ConstraintViolation> validateLastName(String name) {
        if (name.matches(LAST_NAME_REGEX)) {
            return Optional.empty();
        } else {
            return Optional.of(new ConstraintViolation(Parameter.LAST_NAME, LAST_NAME_MESSAGE));
        }
    }

    public Optional<ConstraintViolation> validatePositionId(String positionIdStr) {
        try {
            int positionId = Integer.parseInt(positionIdStr);
            Optional<Position> position = Position.resolvePositionById(positionId);
            if (position.isPresent())
                return Optional.empty();
            else
                return Optional.of(new ConstraintViolation(Parameter.MOVIE_GENRES, String.format(POSITION_DOES_NOT_EXIST_MESSAGE, positionId)));

        } catch (NumberFormatException e) {
            return Optional.of(new ConstraintViolation(Parameter.MOVIE_GENRES, POSITION_MESSAGE));
        }
    }

    public Set<ConstraintViolation> validateData(String firstName, String lastName) {
        Set<ConstraintViolation> violations = new HashSet<>();
        validateFirstName(firstName).ifPresent(violations::add);
        validateLastName(lastName).ifPresent(violations::add);
        return violations;
    }

    public Set<ConstraintViolation> validateData(String firstName, String lastName, String[] positionIds) {
        Set<ConstraintViolation> violations = new HashSet<>();
        validateFirstName(firstName).ifPresent(violations::add);
        validateLastName(lastName).ifPresent(violations::add);
        for (String positionId : positionIds) {
            validatePositionId(positionId).ifPresent(violations::add);
        }
        return violations;
    }
}
