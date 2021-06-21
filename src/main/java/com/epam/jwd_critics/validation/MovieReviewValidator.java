package com.epam.jwd_critics.validation;

import com.epam.jwd_critics.controller.command.Parameter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MovieReviewValidator {
    private static final String TEXT_MESSAGE = "Review text must be at least 100 characters";
    private static final String SCORE_MESSAGE = "Review score must range from 0 to 100";
    private static final String ID_MESSAGE = "Id must be an integer";

    private static boolean isInteger(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

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

    public Optional<ConstraintViolation> validateUserId(String idStr) {
        if (isInteger(idStr))
            return Optional.empty();
        return Optional.of(new ConstraintViolation(Parameter.USER_ID, ID_MESSAGE));
    }

    public Optional<ConstraintViolation> validateMovieId(String idStr) {
        if (isInteger(idStr))
            return Optional.empty();
        return Optional.of(new ConstraintViolation(Parameter.MOVIE_ID, ID_MESSAGE));
    }

    public Set<ConstraintViolation> validateReview(String userId, String movieId, String text, String score) {
        Set<ConstraintViolation> violations = new HashSet<>();
        validateUserId(userId).ifPresent(violations::add);
        validateMovieId(movieId).ifPresent(violations::add);
        validateText(text).ifPresent(violations::add);
        validateScore(score).ifPresent(violations::add);
        return violations;
    }
}
