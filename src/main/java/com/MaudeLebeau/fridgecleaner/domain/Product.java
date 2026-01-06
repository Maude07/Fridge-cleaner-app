package com.MaudeLebeau.fridgecleaner.domain;

import java.util.Objects;

public class Product {
    private final Long id;
    private final String name;
    private Unit unit;

    public Product(Long id, String name, Unit unit) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Product must have a name");
        this.unit = Objects.requireNonNull(unit, "Product must have a unit");
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Unit getUnit() {
        return unit;
    }

}
