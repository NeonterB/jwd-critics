package com.epam.jwd_critics.entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class Movie extends AbstractBaseEntity {
    private String name;
    private String summary;
    private Duration runtime;
    private Country country;
    private Integer rating;
    private Integer reviewCount;
    private LocalDateTime releaseDate;

    private Genre genre;
    private AgeRestriction ageRestriction;
    Map<Celebrity, Position> staff;

    private Movie() {

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

    public Duration getRuntime() {
        return runtime;
    }

    public void setRuntime(Duration runtime) {
        this.runtime = runtime;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
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

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public AgeRestriction getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(AgeRestriction ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    public Map<Celebrity, Position> getStaff() {
        return staff;
    }

    public void setStaff(Map<Celebrity, Position> staff) {
        this.staff = staff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Movie movie = (Movie) o;
        return name.equals(movie.name) && Objects.equals(runtime, movie.runtime) && country == movie.country && Objects.equals(releaseDate, movie.releaseDate) && genre == movie.genre && Objects.equals(staff, movie.staff);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, runtime, country, releaseDate, genre, staff);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", runtime=" + runtime +
                ", country=" + country +
                ", rating=" + rating +
                ", reviewCount=" + reviewCount +
                ", releaseDate=" + releaseDate +
                ", genre=" + genre +
                ", ageRestriction=" + ageRestriction +
                ", staff=" + staff +
                '}';
    }

    public static MovieBuilder newBuilder() {
        return new Movie().new MovieBuilder();
    }

    public class MovieBuilder {

        private MovieBuilder() {
            // private constructor
        }

        public MovieBuilder setId(Integer id) {
            Movie.this.id = id;
            return this;
        }

        public MovieBuilder setName(String name) {
            Movie.this.name = name;
            return this;
        }

        public MovieBuilder setSummary(String summary) {
            Movie.this.summary = summary;
            return this;
        }

        public MovieBuilder setRuntime(Duration runtime) {
            Movie.this.runtime = runtime;
            return this;
        }

        public MovieBuilder setCountry(Country country) {
            Movie.this.country = country;
            return this;
        }

        public MovieBuilder setRating(Integer rating) {
            Movie.this.rating = rating;
            return this;
        }

        public MovieBuilder setReviewCount(Integer reviewCount) {
            Movie.this.reviewCount = reviewCount;
            return this;
        }

        public MovieBuilder setReleaseDate(LocalDateTime releaseDate) {
            Movie.this.releaseDate = releaseDate;
            return this;
        }

        public MovieBuilder setGenre(Genre genre) {
            Movie.this.genre = genre;
            return this;
        }

        public MovieBuilder setAgeRestriction(AgeRestriction ageRestriction) {
            Movie.this.ageRestriction = ageRestriction;
            return this;
        }

        public MovieBuilder setStaff(Map<Celebrity, Position> staff) {
            Movie.this.staff = staff;
            return this;
        }

        public Movie build() {
            Movie movie = new Movie();
            movie.id = Movie.this.id;
            movie.name = Movie.this.name;
            movie.summary = Movie.this.summary;
            movie.runtime = Movie.this.runtime;
            movie.country = Movie.this.country;
            movie.rating = Movie.this.rating;
            movie.reviewCount = Movie.this.reviewCount;
            movie.releaseDate = Movie.this.releaseDate;
            movie.genre = Movie.this.genre;
            movie.ageRestriction = Movie.this.ageRestriction;
            movie.staff = Movie.this.staff;
            return movie;
        }
    }
}
