package com.epam.jwd_critics.dto;

import com.epam.jwd_critics.entity.MovieReview;

public class MovieReviewDTO {
    private Integer score;
    private String text;
    private String userImagePath;
    private String userName;
    private Integer userId;
    private Integer id;

    public MovieReviewDTO(MovieReview review){
        this.score = review.getScore();
        this.text = review.getText();
        this.userId = review.getUserId();
        this.id = review.getId();
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserImagePath() {
        return userImagePath;
    }

    public void setUserImagePath(String userImagePath) {
        this.userImagePath = userImagePath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
