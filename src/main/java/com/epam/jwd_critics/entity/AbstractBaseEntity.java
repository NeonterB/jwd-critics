package com.epam.jwd_critics.entity;

public abstract class AbstractBaseEntity implements BaseEntity {
    @Column(name = "id")
    protected int id;

    public AbstractBaseEntity() {
    }

    public AbstractBaseEntity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
