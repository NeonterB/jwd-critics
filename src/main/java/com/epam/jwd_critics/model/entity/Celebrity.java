package com.epam.jwd_critics.model.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Celebrity extends AbstractBaseEntity {
    private static final String DEFAULT_PROFILE_IMAGE = "c:\\Multimedia\\Programming\\Idea\\jwd-critics\\src\\main\\resources\\assets\\default_profile.jpg";

    @Column(name = "first_name")
    @NotNull(message = "First name can't be null")
    @Pattern(regexp = "^[A-Z][a-z]{1,14}", message = "First name contains illegal characters")
    private String firstName;

    @Column(name = "last_name")
    @NotNull(message = "Last name can't be null")
    @Pattern(regexp = "^[A-Z][a-z]{1,14}", message = "Last name contains illegal characters")
    private String lastName;

    @Column(name = "image_path")
    @Pattern(regexp = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)", message = "Image path is not valid")
    private String imagePath;

    private Map<Movie, List<Position>> jobs;

    private Celebrity() {
    }

    public static CelebrityBuilder newBuilder() {
        return new Celebrity().new CelebrityBuilder();
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

    public Map<Movie, List<Position>> getJobs() {
        return jobs;
    }

    public void setJobs(Map<Movie, List<Position>> jobs) {
        this.jobs = jobs;
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
        Celebrity celebrity = (Celebrity) o;
        return firstName.equals(celebrity.firstName) && lastName.equals(celebrity.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public String toString() {
        return "Celebrity{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", jobs=" + jobs +
                '}';
    }

    public class CelebrityBuilder {
        private CelebrityBuilder() {

        }

        public CelebrityBuilder setId(Integer id) {
            Celebrity.this.id = id;
            return this;
        }

        public CelebrityBuilder setFirstName(String firstName) {
            Celebrity.this.firstName = firstName;
            return this;
        }

        public CelebrityBuilder setLastName(String lastName) {
            Celebrity.this.lastName = lastName;
            return this;
        }

        public CelebrityBuilder setJobs(Map<Movie, List<Position>> jobs) {
            Celebrity.this.jobs = jobs;
            return this;
        }

        public CelebrityBuilder setImagePath(String imagePath) {
            Celebrity.this.imagePath = imagePath;
            return this;
        }

        public Celebrity build() {
            Celebrity celebrity = new Celebrity();
            celebrity.id = Celebrity.this.id;
            celebrity.firstName = Celebrity.this.firstName;
            celebrity.lastName = Celebrity.this.lastName;
            celebrity.imagePath = (Celebrity.this.imagePath == null) ? (DEFAULT_PROFILE_IMAGE) : (Celebrity.this.imagePath);
            return celebrity;
        }
    }
}
