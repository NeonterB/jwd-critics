package com.epam.jwd_critics.entity;

import java.util.Objects;

public class Celebrity extends AbstractBaseEntity {
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

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
                '}';
    }

    public static CelebrityBuilder newBuilder() {
        return new Celebrity().new CelebrityBuilder();
    }

    public class CelebrityBuilder {
        private CelebrityBuilder() {

        }

        public CelebrityBuilder setId(Integer id) {
            Celebrity.this.setId(id);
            return this;
        }

        public CelebrityBuilder setFirstName(String firstName) {
            Celebrity.this.setFirstName(firstName);
            return this;
        }

        public CelebrityBuilder setLastName(String lastName) {
            Celebrity.this.setLastName(lastName);
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
