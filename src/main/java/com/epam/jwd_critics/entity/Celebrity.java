package com.epam.jwd_critics.entity;

import javafx.geometry.Pos;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Celebrity extends AbstractBaseEntity {
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private Map<Movie, List<Position>> jobs;

    private Celebrity() {
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Celebrity celebrity = (Celebrity) o;
        return firstName.equals(celebrity.firstName) && lastName.equals(celebrity.lastName) && jobs.equals(celebrity.jobs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, jobs);
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

    public static CelebrityBuilder newBuilder() {
        return new Celebrity().new CelebrityBuilder();
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

        public Celebrity build() {
            Celebrity celebrity = new Celebrity();
            celebrity.setId(Celebrity.this.id);
            celebrity.setFirstName(Celebrity.this.firstName);
            celebrity.setLastName(Celebrity.this.lastName);
            return celebrity;
        }
    }
}
