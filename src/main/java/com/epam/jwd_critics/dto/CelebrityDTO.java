package com.epam.jwd_critics.dto;

import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.Position;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CelebrityDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String imagePath;

    private Map<Movie, List<Position>> jobs;

    public CelebrityDTO(Celebrity celebrity) {
        this.id = celebrity.getId();
        this.firstName = celebrity.getFirstName();
        this.lastName = celebrity.getLastName();
        this.imagePath = celebrity.getImagePath();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Map<Movie, List<Position>> getJobs() {
        return jobs;
    }

    public void setJobs(Map<Movie, List<Position>> jobs) {
        this.jobs = jobs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CelebrityDTO that = (CelebrityDTO) o;
        return id == that.id && firstName.equals(that.firstName) && lastName.equals(that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }
}
