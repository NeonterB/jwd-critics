package com.epam.jwd_critics.entity;

import com.epam.jwd_critics.exception.UnknownEntityException;

import java.util.Arrays;

public enum Position implements BaseEntity{
    DIRECTOR(1L),
    ACTOR(2L),
    WRITER(3L),
    PRODUCER(4L);
    private final Long id;

    Position(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public static Position resolvePositionById(int id) {
        return Arrays.stream(Position.values())
                .filter(rank -> rank.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UnknownEntityException("AgeRestriction", id));
    }
}
