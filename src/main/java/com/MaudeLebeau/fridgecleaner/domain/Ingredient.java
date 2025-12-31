package com.MaudeLebeau.fridgecleaner.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Ingredient {
    private final Long id;
    private final String itemName;
    private BigDecimal quantity;
    private Unit unit;

    public Ingredient(Long id, String itemName, BigDecimal quantity, Unit unit) {
        this.id = id;
        this.itemName = Objects.requireNonNull(itemName, "Item must have a name");
        this.quantity = Objects.requireNonNull(quantity, "Recipe must have a quantity");
        this.unit = Objects.requireNonNull(unit, "Ingredient must have a unit");

        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quantity must be >= than zero");
        }
    }

    public Long getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public Unit getUnit() {
        return unit;
    }
}
