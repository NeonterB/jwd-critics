package com.epam.jwd_critics.entity;

public abstract class AbstractBaseEntity implements BaseEntity {
    @Column(columnName = "id")
    protected Integer id;

    public AbstractBaseEntity() {
    }

    public AbstractBaseEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
