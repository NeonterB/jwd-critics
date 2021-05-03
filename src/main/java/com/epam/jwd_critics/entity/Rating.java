package com.epam.jwd_critics.entity;

import com.epam.jwd_critics.exception.UnknownEntityException;

import java.util.Arrays;

public enum Rating implements BaseEntity{
    LOW(1),
    MEDIUM(2),
    HIGH(3);
    private final Integer id;

    Rating(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public static Rating resolveRatingById(int id) {
        return Arrays.stream(Rating.values())
                .filter(rank -> rank.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UnknownEntityException("AgeRestriction", id));
    }
}
