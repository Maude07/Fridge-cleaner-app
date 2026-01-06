package com.MaudeLebeau.fridgecleaner.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Item {
    private final Long id;
    private Product product;
    private BigDecimal quantity;
    private Unit unit;
    private LocalDate expiryDate;
    private LocalDateTime creationDate;

    public Item(Long id, Product product,  BigDecimal quantity, Unit unit, LocalDate expiryDate) {
        this.id = id;
        this.product = Objects.requireNonNull(product, "Item must reference a product");
        this.quantity = Objects.requireNonNull(quantity, "Item must have a quantity");
        this.unit = Objects.requireNonNull(unit, "Item must have a unit");
        this.expiryDate = expiryDate;
        this.creationDate = LocalDateTime.now();

        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quantity must be >= than zero");
        }

    }

    public Item(Long id, Product product, BigDecimal quantity, Unit unit, LocalDate expiryDate, LocalDateTime creationDate) {
        this.id = id;
        this.product = Objects.requireNonNull(product, "Item must reference a product");
        this.quantity = Objects.requireNonNull(quantity, "Item must have a quantity");
        this.unit = Objects.requireNonNull(unit, "Item must have a unit");
        this.expiryDate = expiryDate;
        this.creationDate = Objects.requireNonNull(creationDate, "Item must have a date");

        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quantity must be >= than zero");
        }

    }

    public Long getId() {
        return id;
    }

    public Product getProduct() { return product; }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}
