package com.epam.jwd_critics.entity;

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

}
