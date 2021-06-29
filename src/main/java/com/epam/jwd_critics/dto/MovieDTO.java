package com.epam.jwd_critics.dto;

import com.epam.jwd_critics.entity.Movie;

import java.util.Objects;

public class MovieDTO {
    private int id;
    private String name;
    private int rating;
    private String imagePath;

    public MovieDTO() {
    }

    public MovieDTO(Movie movie) {
        this.id = movie.getId();
        this.name = movie.getName();
        this.rating = movie.getRating();
        this.imagePath = movie.getImagePath();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieDTO movieDTO = (MovieDTO) o;
        return id == movieDTO.id && rating == movieDTO.rating && name.equals(movieDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, rating);
    }
}
