package com.epam.jwd_critics.dto;

import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.entity.Role;
import com.epam.jwd_critics.entity.Status;
import com.epam.jwd_critics.entity.User;

import java.util.List;

public class UserDTO {
    private int id;
    private int reviewCount;
    private Role role;
    private Status status;
    private String firstName;
    private String lastName;
    private String imagePath;

    private List<MovieReviewDTO> reviews;

    public UserDTO(User user) {
        this.id = user.getId();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.imagePath = user.getImagePath();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<MovieReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<MovieReviewDTO> reviews) {
        reviewCount = reviews.size();
        this.reviews = reviews;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
}
