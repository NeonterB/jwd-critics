package com.epam.jwd_critics.entity;

public class Celebrity extends AbstractBaseEntity{
    private String name;

    Celebrity(Long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
