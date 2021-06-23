package com.epam.jwd_critics.entity;

@Column(name = "status_id")
public enum Status implements BaseEntity {
    ACTIVE(1),
    INACTIVE(2),
    BANNED(3);
    private final int id;

    Status(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

}
