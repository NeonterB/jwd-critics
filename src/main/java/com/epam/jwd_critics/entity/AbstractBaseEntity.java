package com.epam.jwd_critics.entity;

public abstract class AbstractBaseEntity implements BaseEntity {
    private final Long id;

    AbstractBaseEntity(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
