package com.epam.jwd_critics.validation;

import com.epam.jwd_critics.controller.command.Parameter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserValidator {
    private static final String FIRST_NAME_REGEX = "^[A-Z][a-z]{1,14}";
    private static final String LAST_NAME_REGEX = "^[A-Z][a-z]{1,14}";
    private static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
            "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@" +
            "(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
            "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private static final String LOGIN_REGEX = "^[a-zA-Z0-9._-]{3,25}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";

    private static final String FIRST_NAME_MESSAGE = "First name must contain letters only, must start with an uppercase letter";
    private static final String LAST_NAME_MESSAGE = "Last name must contain letters only, must start with an uppercase letter";
    private static final String EMAIL_MESSAGE = "Invalid email";
    private static final String LOGIN_MESSAGE = "Minimum 3 and maximum 25 characters, can contain letters, numbers, and special characters \"._-\"";
    private static final String PASSWORD_MESSAGE = "Minimum 8 and maximum 20 characters, at least one uppercase letter, one lowercase letter, one number and one special character \"@$!%*?&\"";

    public Optional<ConstraintViolation> validateFirstName(String firstName) {
        if (firstName.matches(FIRST_NAME_REGEX)) {
            return Optional.empty();
        } else {
            return Optional.of(new ConstraintViolation(Parameter.FIRST_NAME, FIRST_NAME_MESSAGE));
        }
    }

    public Optional<ConstraintViolation> validateLastName(String lastName) {
        if (lastName.matches(LAST_NAME_REGEX)) {
            return Optional.empty();
        } else {
            return Optional.of(new ConstraintViolation(Parameter.LAST_NAME, LAST_NAME_MESSAGE));
        }
    }

    public Optional<ConstraintViolation> validateEmail(String email) {
        if (email.matches(EMAIL_REGEX)) {
            return Optional.empty();
        } else {
            return Optional.of(new ConstraintViolation(Parameter.EMAIL, EMAIL_MESSAGE));
        }
    }

    public Optional<ConstraintViolation> validateLogin(String login) {
        if (login.matches(LOGIN_REGEX)) {
            return Optional.empty();
        } else {
            return Optional.of(new ConstraintViolation(Parameter.LOGIN, LOGIN_MESSAGE));
        }
    }

    public Optional<ConstraintViolation> validatePassword(String password) {
        if (password.matches(PASSWORD_REGEX)) {
            return Optional.empty();
        } else {
            return Optional.of(new ConstraintViolation(Parameter.PASSWORD, PASSWORD_MESSAGE));
        }
    }

    public Set<ConstraintViolation> validateLogInData(String login, String password) {
        Set<ConstraintViolation> violations = new HashSet<>();
        validateLogin(login).ifPresent(violations::add);
        validatePassword(password).ifPresent(violations::add);
        return violations;
    }

    public Set<ConstraintViolation> validateRegistrationData(String firstName, String lastName, String email, String login, String password) {
        Set<ConstraintViolation> violations = new HashSet<>();
        validateFirstName(firstName).ifPresent(violations::add);
        validateLastName(lastName).ifPresent(violations::add);
        validateEmail(email).ifPresent(violations::add);
        validateLogin(login).ifPresent(violations::add);
        validatePassword(password).ifPresent(violations::add);
        return violations;
    }
}
