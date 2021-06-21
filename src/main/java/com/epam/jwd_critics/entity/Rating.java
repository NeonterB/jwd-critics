package com.epam.jwd_critics.entity;

@Column(name = "rating_id")
public enum Rating implements BaseEntity {
    LOW(1),
    MEDIUM(2),
    HIGH(3);
    private final Integer id;

    Rating(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

}
