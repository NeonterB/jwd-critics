package com.epam.jwd_critics.entity;

public class Celebrity extends AbstractBaseEntity{
    private String name;

    Celebrity(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
