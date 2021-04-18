package com.epam.jwd_critics.entity;

import com.epam.jwd_critics.exception.UnknownEntityException;

import java.util.Arrays;

public enum Genre implements BaseEntity {
    ACTION(1L),
    ADVENTURE(2L),
    CARTOON(3L),
    COMEDY(4L),
    CRIME(5L),
    DOCUMENTARY(6L),
    DRAMA(7L),
    FANTASY(8L),
    HORROR(9L),
    ROMANCE(10L),
    THRILLER(11L);
    private final Long id;

    Genre(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public static Genre resolveGenreById(int id) {
        return Arrays.stream(Genre.values())
                .filter(rank -> rank.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UnknownEntityException("Rank", id));
    }
}
