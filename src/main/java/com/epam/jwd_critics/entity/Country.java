package com.epam.jwd_critics.entity;

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

    @Override
    public int getId() {
        return id;
    }

}
