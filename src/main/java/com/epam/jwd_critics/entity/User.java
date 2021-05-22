package com.epam.jwd_critics.entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class User extends AbstractBaseEntity {
    @Column(name = "first_name")
    @NotNull(message = "First name can't be null")
    @Pattern(regexp = "^[A-Z][a-z]{1,14}", message = "First name contains illegal characters")
    private String firstName;

    @Column(name = "last_name")
    @NotNull(message = "Last name can't be null")
    @Pattern(regexp = "^[A-Z][a-z]{1,14}", message = "Last name contains illegal characters")
    private String lastName;

    @Column(name = "email")
    @Email(message = "Email is invalid")
    private String email;

    @Column(name = "login")
    @NotNull(message = "Login can't be null")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,25}$", message = "Login doesn't meet requirements")
    private String login;

    @Column(name = "password")
    @NotNull(message = "Password can't be null")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$",
            message = "Password doesn't meet safety requirements")
    private String password;

    @Column(name = "rating")
    @NotNull(message = "Rating can't be null")
    @Max(value = 100, message = "Rating can't be greater then 100")
    @Positive(message = "User rating must be positive")
    private int rating;

    @Column(name = "status_id")
    @NotNull(message = "Status can't be null")
    private Status status;

    @Column(name = "role_id")
    @NotNull(message = "Role can't be null")
    private Role role;

    @Column(name = "image_path")
    @Pattern(regexp = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)", message = "Image path is not valid")
    private String imagePath;

    private List<MovieReview> reviews;

    private User() {

    }

    public static UserBuilder newBuilder() {
        return new User().new UserBuilder();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<MovieReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<MovieReview> reviews) {
        this.reviews = reviews;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, login, password, rating, status, role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return rating == user.rating && firstName.equals(user.firstName) && lastName.equals(user.lastName) && email.equals(user.email) && login.equals(user.login) && password.equals(user.password) && status == user.status && role == user.role;
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

        public UserBuilder setImagePath(String imagePath) {
            User.this.imagePath = imagePath;
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
            user.reviews = Collections.emptyList();
            user.imagePath = User.this.imagePath;
            return user;
        }
    }
}
