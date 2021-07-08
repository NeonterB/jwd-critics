package com.epam.jwd_critics.entity;

import java.util.Arrays;
import java.util.Optional;

@Column(name = "genre_id")
public enum Genre implements BaseEntity {
    ACTION(1),
    ADVENTURE(2),
    ANIMATION(3),
    COMEDY(4),
    CRIME(5),
    DOCUMENTARY(6),
    DRAMA(7),
    FANTASY(8),
    HORROR(9),
    THRILLER(10),
    SCI_FI(11),
    WESTERN(12),
    HISTORY(13),
    ROMANCE(14);
    private final int id;

    Genre(int id) {
        this.id = id;
    }

    public static Optional<Genre> resolveGenreById(int id) {
        return Arrays.stream(Genre.values()).filter(g -> g.id == id).findFirst();
    }

    @Override
    public int getId() {
        return id;
    }
}
