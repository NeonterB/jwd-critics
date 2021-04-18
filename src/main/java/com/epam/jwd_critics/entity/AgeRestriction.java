package com.epam.jwd_critics.entity;

import com.epam.jwd_critics.exception.UnknownEntityException;

import java.util.Arrays;

public enum AgeRestriction implements BaseEntity{
    G(1L),
    PG(2L),
    PG_13(3L),
    R(4L),
    NC_17(5L);
    private final Long id;

    AgeRestriction(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public static AgeRestriction resolveAgeRestrictionById(int id) {
        return Arrays.stream(AgeRestriction.values())
                .filter(rank -> rank.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UnknownEntityException("AgeRestriction", id));
    }
}
