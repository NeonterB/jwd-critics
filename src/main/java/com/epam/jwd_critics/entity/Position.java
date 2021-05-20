package com.epam.jwd_critics.entity;

import com.epam.jwd_critics.exception.UnknownEntityException;

import java.util.Arrays;

@Column(name = "position_id")
public enum Position implements BaseEntity {
    DIRECTOR(1),
    ACTOR(2),
    WRITER(3),
    PRODUCER(4);
    private final Integer id;

    Position(Integer id) {
        this.id = id;
    }

    public static Position resolvePositionById(int id) {
        return Arrays.stream(Position.values())
                .filter(rank -> rank.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UnknownEntityException("AgeRestriction", id));
    }

    @Override
    public Integer getId() {
        return id;
    }
}