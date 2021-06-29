package com.epam.jwd_critics.validation;

import com.epam.jwd_critics.controller.command.Parameter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CelebrityValidator {
    private static final String FIRST_NAME_REGEX = "^[A-Z][a-z]{1,14}";

    private static final String NAME_MESSAGE = "First and last names must contain letters only, must start with an uppercase letter";

    public Optional<ConstraintViolation> validateName(String name) {
        if (name.matches(FIRST_NAME_REGEX)) {
            return Optional.empty();
        } else {
            return Optional.of(new ConstraintViolation(Parameter.FIRST_NAME, NAME_MESSAGE));
        }
    }

    public Set<ConstraintViolation> validateData(String firstName, String lastName) {
        Set<ConstraintViolation> violations = new HashSet<>();
        validateName(firstName).ifPresent(violations::add);
        validateName(lastName).ifPresent(violations::add);
        return violations;
    }
}
