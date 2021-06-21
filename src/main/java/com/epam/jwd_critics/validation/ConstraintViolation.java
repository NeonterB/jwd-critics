package com.epam.jwd_critics.validation;

import com.epam.jwd_critics.controller.command.Parameter;

import java.util.Objects;

public class ConstraintViolation {
    private final Parameter parameter;
    private final String message;

    public ConstraintViolation(Parameter parameter, String message) {
        this.parameter = parameter;
        this.message = message;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstraintViolation that = (ConstraintViolation) o;
        return parameter == that.parameter && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameter, message);
    }

    @Override
    public String toString() {
        return "ConstraintViolation{" +
                "parameter=" + parameter +
                ", message='" + message + '\'' +
                '}';
    }
}
