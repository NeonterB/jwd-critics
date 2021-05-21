package com.epam.jwd_critics.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.Objects;

public class MovieReview extends AbstractBaseEntity {
    @Column(name = "user_id")
    private final Integer userId;

    @Column(name = "movie_id")
    private final Integer movieId;

    @Column(name = "text")
    @NotNull(message = "Review text can't be null")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё0-9\\\\s()\"':?!.,-]{1,10000}$",
            message = "Review text contains illegal characters")
    private String text;

    @Column(name = "score")
    @NotNull(message = "Score can't be null")
    @Max(value = 100, message = "Score value can't be greater than 100")
    @Positive(message = "Score value must be positive")
    private Integer score;

    public MovieReview(String text, Integer score, Integer userId, Integer movieId) {
        this.text = text;
        this.score = score;
        this.userId = userId;
        this.movieId = movieId;
    }

    public MovieReview(Integer id, String text, Integer score, Integer userId, Integer movieId) {
        super(id);
        this.text = text;
        this.score = score;
        this.userId = userId;
        this.movieId = movieId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, score, userId, movieId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieReview that = (MovieReview) o;
        return text.equals(that.text) && score.equals(that.score) && userId.equals(that.userId) && movieId.equals(that.movieId);
    }
}
