package com.epam.jwd_critics.entity;

import com.epam.jwd_critics.exception.UnknownEntityException;

import java.util.Arrays;

public enum Status implements BaseEntity{
    BANNED(1L),
    ACTIVE(2L),
    INACTIVE(3L);
    private final Long id;

    Status(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public static Status resolveStatusById(int id) {
        return Arrays.stream(Status.values())
                .filter(rank -> rank.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UnknownEntityException("AgeRestriction", id));
    }
}
