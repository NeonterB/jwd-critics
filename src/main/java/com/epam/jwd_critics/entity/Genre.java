package com.epam.jwd_critics.entity;

import com.epam.jwd_critics.exception.UnknownEntityException;

import java.util.Arrays;

@Column(name = "genre_id")
public enum Genre implements BaseEntity {
    ACTION(1),
    ADVENTURE(2),
    CARTOON(3),
    COMEDY(4),
    CRIME(5),
    DOCUMENTARY(6),
    DRAMA(7),
    FANTASY(8),
    HORROR(9),
    ROMANCE(10),
    THRILLER(11);
    private final Integer id;

    Genre(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public static Genre resolveGenreById(int id) {
        return Arrays.stream(Genre.values())
                .filter(rank -> rank.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UnknownEntityException("Rank", id));
    }
}
