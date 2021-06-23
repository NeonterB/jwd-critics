package com.epam.jwd_critics.entity;

import com.epam.jwd_critics.exception.UnknownEntityException;

import java.util.Arrays;

@Column(name = "position")
public enum Position implements BaseEntity {
    DIRECTOR(1),
    ACTOR(2),
    WRITER(3),
    PRODUCER(4);
    private final int id;

    Position(int id) {
        this.id = id;
    }

    public static Position resolvePositionById(int id) {
        return Arrays.stream(Position.values())
                .filter(rank -> rank.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UnknownEntityException("AgeRestriction", id));
    }

    @Override
    public int getId() {
        return id;
    }
}