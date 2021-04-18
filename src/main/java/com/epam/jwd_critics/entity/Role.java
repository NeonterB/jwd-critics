package com.epam.jwd_critics.entity;

import com.epam.jwd_critics.exception.UnknownEntityException;

import java.util.Arrays;

public enum Role implements BaseEntity{
    ADMIN(1L),
    HELPER(2L),
    USER(3L),
    GUEST(4L);
    private final Long id;

    Role(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public static Role resolveRoleById(int id) {
        return Arrays.stream(Role.values())
                .filter(rank -> rank.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UnknownEntityException("AgeRestriction", id));
    }
}
