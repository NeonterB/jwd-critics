package com.epam.jwd_critics.validation;

import com.epam.jwd_critics.controller.command.Parameter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CelebrityValidator {
    private static final String NAME_REGEX = "^[A-Z][a-z]{1,14}";

    private static final String NAME_MESSAGE = "First and last names must contain letters only, must start with an uppercase letter";

    public Optional<ConstraintViolation> validateFirstName(String name) {
        return validateName(name, Parameter.FIRST_NAME);
    }

    public Optional<ConstraintViolation> validateLastName(String name) {
        return validateName(name, Parameter.LAST_NAME);
    }

    private Optional<ConstraintViolation> validateName(String name, Parameter parameter) {
        if (name.matches(NAME_REGEX)) {
            return Optional.empty();
        } else {
            return Optional.of(new ConstraintViolation(parameter, NAME_MESSAGE));
        }
    }

    public Set<ConstraintViolation> validateData(String firstName, String lastName) {
        Set<ConstraintViolation> violations = new HashSet<>();
        validateFirstName(firstName).ifPresent(violations::add);
        validateLastName(lastName).ifPresent(violations::add);
        return violations;
    }
}
