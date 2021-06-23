package com.epam.jwd_critics.entity;

import java.util.Objects;

public class MovieReview extends AbstractBaseEntity {
    @Column(name = "user_id")
    private final int userId;

    @Column(name = "movie_id")
    private final int movieId;

    @Column(name = "text")
    private String text;

    @Column(name = "score")
    private int score;

    public MovieReview(String text, int score, int userId, int movieId) {
        this.text = text;
        this.score = score;
        this.userId = userId;
        this.movieId = movieId;
    }

    public MovieReview(int id, String text, int score, int userId, int movieId) {
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getUserId() {
        return userId;
    }

    public int getMovieId() {
        return movieId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieReview that = (MovieReview) o;
        return userId == that.userId && movieId == that.movieId && score == that.score && text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, movieId, text, score);
    }
}
