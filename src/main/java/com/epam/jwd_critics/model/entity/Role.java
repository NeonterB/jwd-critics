package com.epam.jwd_critics.model.entity;

import com.epam.jwd_critics.exception.UnknownEntityException;

import java.util.Arrays;

@Column(name = "role_id")
public enum Role implements BaseEntity {
    ADMIN(1),
    HELPER(2),
    USER(3),
    GUEST(4);
    private final Integer id;

    Role(Integer id) {
        this.id = id;
    }

    public static Role resolveRoleById(int id) {
        return Arrays.stream(Role.values())
                .filter(rank -> rank.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UnknownEntityException("AgeRestriction", id));
    }

    @Override
    public Integer getId() {
        return id;
    }
}
