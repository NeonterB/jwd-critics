package com.epam.jwd_critics.model.entity;


import com.epam.jwd_critics.exception.UnknownEntityException;

import java.util.Arrays;

@Column(name = "age_restriction_id")
public enum AgeRestriction implements BaseEntity {
    G(1),
    PG(2),
    PG_13(3),
    R(4),
    NC_17(5);
    private final Integer id;

    AgeRestriction(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

}
