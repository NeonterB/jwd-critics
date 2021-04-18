package com.epam.jwd_critics.entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class Movie extends AbstractBaseEntity {
    private String name;
    private String summary;
    private Duration runtime;
    private Integer rating;
    private Integer reviewCount;
    private LocalDateTime releaseDate;

    private Genre genre;
    private AgeRestriction ageRestriction;
    Map<Celebrity, Position> staff;

    Movie(Long id) {
        super(id);
    }
}
