package com.epam.jwd_critics.model.entity;

import com.epam.jwd_critics.exception.UnknownEntityException;

import java.util.Arrays;

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
    private final Integer id;

    Country(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

}
