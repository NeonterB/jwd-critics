package com.epam.jwd_critics.entity;

import java.util.Objects;

public class Celebrity extends AbstractBaseEntity{
    private String name;

    Celebrity(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Celebrity celebrity = (Celebrity) o;
        return name.equals(celebrity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    @Override
    public String toString() {
        return "Celebrity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
