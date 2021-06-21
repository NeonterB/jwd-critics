package com.epam.jwd_critics.model.dto;

import com.epam.jwd_critics.model.entity.Celebrity;
import com.epam.jwd_critics.model.entity.Movie;
import com.epam.jwd_critics.model.entity.Position;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Map;

public class MovieDTO {
    private Integer id;
    private String name;
    private String summary;
    private String runtime;
    private String country;
    private String imagePath;
    private Integer rating;
    private Integer reviewCount;
    private String releaseDate;
    private String ageRestriction;
    private Map<Position, List<Celebrity>> staff;

    public MovieDTO(Movie movie) {
        this.id = movie.getId();
        this.name = movie.getName();
        this.summary = (movie.getSummary() == null) ? ("Unknown") : (movie.getSummary());

        if (movie.getRuntime() == null) {
            this.runtime = "Unknown";
        } else {
            this.runtime = "";
            long hours = movie.getRuntime().toHours();
            this.runtime += (hours > 0) ? ((hours == 1) ? ("1 hour ") : (hours + " hours ")) : ("");
            long minutes = movie.getRuntime().toMinutes() % 60;
            this.runtime += (minutes > 0) ? ((minutes == 1) ? ("1 minute") : (minutes + " minutes")) : ("");
        }

        this.country = (movie.getCountry() == null) ? ("Unknown") : (movie.getCountry().name());
        this.imagePath = movie.getImagePath();
        this.rating = movie.getRating();
        this.reviewCount = movie.getReviewCount();
        this.ageRestriction = (movie.getAgeRestriction() == null) ? ("Unknown") : (movie.getAgeRestriction().name().replace("_", "-"));
        this.releaseDate = (movie.getReleaseDate() == null) ? ("Unknown") : (movie.getReleaseDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
        this.staff = movie.getStaff();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(String ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    public Map<Position, List<Celebrity>> getStaff() {
        return staff;
    }

    public void setStaff(Map<Position, List<Celebrity>> staff) {
        this.staff = staff;
    }
}
