package com.epam.jwd_critics.entity;


import java.util.Arrays;
import java.util.Optional;

@Column(name = "age_restriction_id")
public enum AgeRestriction implements BaseEntity {
    G(1),
    PG(2),
    PG_13(3),
    R(4),
    NC_17(5);
    private final int id;

    AgeRestriction(int id) {
        this.id = id;
    }

    public static Optional<AgeRestriction> resolveAgeRestrictionById(int id) {
        return Arrays.stream(AgeRestriction.values()).filter(c -> c.getId() == id).findFirst();
    }

    @Override
    public int getId() {
        return id;
    }
}
