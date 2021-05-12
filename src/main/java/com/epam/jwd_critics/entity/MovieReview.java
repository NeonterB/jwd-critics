package com.epam.jwd_critics.entity;

import java.util.Objects;

public class MovieReview extends AbstractBaseEntity{
    @Column(name = "text")
    private String text;
    @Column(name = "score")
    private Integer score;
    @Column(name = "user_id")
    private final Integer userId;
    @Column(name = "movie_id")
    private final Integer movieId;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieReview that = (MovieReview) o;
        return text.equals(that.text) && score.equals(that.score) && userId.equals(that.userId) && movieId.equals(that.movieId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, score, userId, movieId);
    }
}
