package com.epam.jwd_critics.entity;

import java.util.Objects;

public class User extends AbstractBaseEntity {
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;
    @Column(name = "rating")
    private int rating;

    @Column(name = "status_id")
    private Status status;
    @Column(name = "role_id")
    private Role role;

    private User() {

    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Status getStatus() {
        return status;
    }

    public int getRating() {
        return rating;
    }

    public Role getRole() {
        return role;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static UserBuilder newBuilder() {
        return new User().new UserBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return rating == user.rating && firstName.equals(user.firstName) && lastName.equals(user.lastName) && email.equals(user.email) && login.equals(user.login) && password.equals(user.password) && status == user.status && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, email, login, password, rating, status, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", rating=" + rating +
                ", status=" + status +
                ", role=" + role +
                '}';
    }

    public class UserBuilder {

        private UserBuilder() {
            // private constructor
        }

        public UserBuilder setId(Integer id) {
            User.this.id = id;
            return this;
        }

        public UserBuilder setFirstName(String firstName) {
            User.this.firstName = firstName;
            return this;
        }

        public UserBuilder setLastName(String lastName) {
            User.this.lastName = lastName;
            return this;
        }

        public UserBuilder setLogin(String login) {
            User.this.login = login;
            return this;
        }

        public UserBuilder setPassword(String password) {
            User.this.password = password;
            return this;
        }

        public UserBuilder setEmail(String email) {
            User.this.email = email;
            return this;
        }

        public UserBuilder setRating(Integer rating) {
            User.this.rating = rating;
            return this;
        }

        public UserBuilder setRole(Integer roleId) {
            User.this.role = Role.resolveRoleById(roleId);
            return this;
        }

        public UserBuilder setStatus(Integer statusId) {
            User.this.status = Status.resolveStatusById(statusId);
            return this;
        }

        public User build() {
            User user = new User();
            user.id = User.this.id;
            user.firstName = User.this.firstName;
            user.lastName = User.this.lastName;
            user.email = User.this.email;
            user.login = User.this.login;
            user.password = User.this.password;
            user.rating = User.this.rating;
            user.status = User.this.status;
            user.role = User.this.role;
            return user;
        }
    }
}
