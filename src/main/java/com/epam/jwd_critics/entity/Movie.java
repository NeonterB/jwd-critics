package com.epam.jwd_critics.entity;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Movie extends AbstractBaseEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "summary")
    private String summary;
    @Column(name = "runtime")
    private Duration runtime;
    @Column(name = "country_id")
    private Country country;
    @Column(name = "rating")
    private Integer rating;
    @Column(name = "review_count")
    private Integer reviewCount;
    @Column(name = "release_date")
    private LocalDate releaseDate;

    private List<Genre> genres;
    @Column(name = "age_restriction_id")
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

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
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
        return name.equals(movie.name) && Objects.equals(summary, movie.summary) && Objects.equals(runtime, movie.runtime) && country == movie.country && Objects.equals(releaseDate, movie.releaseDate) && Objects.equals(genres, movie.genres) && ageRestriction == movie.ageRestriction && Objects.equals(staff, movie.staff);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, summary, runtime, country, releaseDate, genres, ageRestriction, staff);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", runtime=" + runtime.toString() +
                ", country=" + country +
                ", rating=" + rating +
                ", reviewCount=" + reviewCount +
                ", releaseDate=" + releaseDate +
                ", genres=" + genres +
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

        public MovieBuilder setReleaseDate(LocalDate releaseDate) {
            Movie.this.releaseDate = releaseDate;
            return this;
        }

        public MovieBuilder setGenres(List<Genre> genres) {
            Movie.this.genres = genres;
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
            movie.genres = Movie.this.genres;
            movie.ageRestriction = Movie.this.ageRestriction;
            movie.staff = Movie.this.staff;
            return movie;
        }
    }
}
