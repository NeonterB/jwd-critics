package com.epam.jwd_critics.validation;

import com.epam.jwd_critics.controller.command.Parameter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MovieReviewValidator {
    private static final String TEXT_MESSAGE = "Review text must be at least 100 characters";
    private static final String SCORE_MESSAGE = "Review score must range from 0 to 100";

    public Optional<ConstraintViolation> validateText(String text) {
        if (text.length() >= 100) {
            return Optional.empty();
        } else {
            return Optional.of(new ConstraintViolation(Parameter.MOVIE_REVIEW_TEXT, TEXT_MESSAGE));
        }
    }

    public Optional<ConstraintViolation> validateScore(String scoreStr) {
        try {
            int score = Integer.parseInt(scoreStr);
            if (score >= 0 && score <= 100) {
                return Optional.empty();
            }
        } catch (NumberFormatException e) {
            //
        }
        return Optional.of(new ConstraintViolation(Parameter.MOVIE_REVIEW_SCORE, SCORE_MESSAGE));
    }

    public Set<ConstraintViolation> validateReview(String text, String score) {
        Set<ConstraintViolation> violations = new HashSet<>();
        validateText(text).ifPresent(violations::add);
        validateScore(score).ifPresent(violations::add);
        return violations;
    }
}
