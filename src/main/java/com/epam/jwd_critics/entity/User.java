package com.epam.jwd_critics.entity;

public class User extends AbstractBaseEntity {
    private String name;
    private String login;
    private String password;

    private Status status;
    private Rating rating;
    private Role role;

    public User(Long id) {
        super(id);
    }
}
