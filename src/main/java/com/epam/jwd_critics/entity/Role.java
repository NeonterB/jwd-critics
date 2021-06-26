package com.epam.jwd_critics.entity;

@Column(name = "role_id")
public enum Role implements BaseEntity {
    ADMIN(1),
    USER(2),
    GUEST(3);
    private final int id;

    Role(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }
}
