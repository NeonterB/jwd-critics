package com.epam.jwd_critics.dto;

import com.epam.jwd_critics.entity.MovieReview;

public class MovieReviewDTO {
    private int score;
    private String text;
    private String imagePath;
    private String title;
    private int userId;
    private int movieId;
    private int id;

    public MovieReviewDTO(MovieReview review){
        this.score = review.getScore();
        this.text = review.getText();
        this.userId = review.getUserId();
        this.movieId = review.getMovieId();
        this.id = review.getId();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
