package com.epam.jwd_critics.entity;

@Column(name = "role_id")
public enum Role implements BaseEntity {
    ADMIN(1),
    HELPER(2),
    USER(3),
    GUEST(4);
    private final int id;

    Role(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }
}
