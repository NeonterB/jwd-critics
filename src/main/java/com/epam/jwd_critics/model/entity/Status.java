package com.epam.jwd_critics.model.entity;

import com.epam.jwd_critics.exception.UnknownEntityException;

import java.util.Arrays;

@Column(name = "status_id")
public enum Status implements BaseEntity {
    ACTIVE(1),
    INACTIVE(2),
    BANNED(3);
    private final Integer id;

    Status(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

}
