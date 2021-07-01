package com.epam.jwd_critics.entity;

import java.util.Arrays;
import java.util.Optional;

@Column(name = "country_id")
public enum Country implements BaseEntity {
    USA(1),
    RUSSIA(2),
    DENMARK(3),
    CANADA(4),
    POLAND(5),
    GERMANY(6),
    BRITAIN(7),
    NORWAY(8),
    FINLAND(9),
    SWEDEN(10);
    private final int id;

    Country(int id) {
        this.id = id;
    }

    public static Optional<Country> resolveCountryByName(String name) {
        return Arrays.stream(Country.values()).filter(c -> c.name().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public int getId() {
        return id;
    }
}
