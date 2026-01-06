package com.MaudeLebeau.fridgecleaner.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Ingredient {
    private Product product;
    private BigDecimal quantity;
    private Unit unit;

    public Ingredient(Product product, BigDecimal quantity, Unit unit) {
        this.product= Objects.requireNonNull(product, "Item must have a name");
        this.quantity = Objects.requireNonNull(quantity, "Recipe must have a quantity");
        this.unit = Objects.requireNonNull(unit, "Ingredient must have a unit");

        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quantity must be >= than zero");
        }
    }

    public Product getProduct() {
        return product;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient other)) return false;
        return product.getId().equals(other.product.getId());
    }
}
