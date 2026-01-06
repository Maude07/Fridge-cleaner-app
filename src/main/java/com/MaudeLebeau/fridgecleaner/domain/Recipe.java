package com.MaudeLebeau.fridgecleaner.domain;

import java.util.List;
import java.util.Objects;

public class Recipe {
    private final Long id;
    private final String name;
    private Integer serving;
    private List<Ingredient> ingredientList;
    private String instructions;

    public Recipe(Long id, String name, Integer serving, String instructions, List<Ingredient> ingredientList) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Recipe must have a name");
        this.instructions = Objects.requireNonNull(instructions, "Recipe must have instructions");
        this.serving = Objects.requireNonNull(serving, "Servings can't be null");
        this.ingredientList = Objects.requireNonNull(ingredientList, "Recipe must have ingredients");

        if (serving <= 0) {
            throw new IllegalArgumentException("Recipe must have at least one serving");
        }
    }

    public Long getId() {
        return id;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getName() {
        return name;
    }

    public Integer getServings() {
        return serving;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }
}
