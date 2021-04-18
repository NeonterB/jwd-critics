package com.epam.jwd_critics.entity;

import com.epam.jwd_critics.exception.UnknownEntityException;

import java.util.Arrays;

public enum Rating implements BaseEntity{
    LOW(1L),
    MEDIUM(2L),
    HIGH(3L);
    private final Long id;

    Rating(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public static Rating resolveRatingById(int id) {
        return Arrays.stream(Rating.values())
                .filter(rank -> rank.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UnknownEntityException("AgeRestriction", id));
    }
}
