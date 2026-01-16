package com.MaudeLebeau.fridgecleaner.domain;

import java.util.List;

public class RecipeMatchResult {
    private Recipe recipe;
    private Integer missingCount;
    private List<Ingredient> missingIngList;
    private RecipeStatus status;

    public RecipeMatchResult(Recipe recipe, Integer missingCount, List<Ingredient> missingIngList, RecipeStatus status) {
        this.recipe = recipe;
        this.missingCount = missingCount;
        this.missingIngList = missingIngList;
        this.status = status;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public Integer getMissingCount() {
        return missingCount;
    }

    public List<Ingredient> getMissingIngList() {
        return missingIngList;
    }

    public RecipeStatus getStatus() {
        return status;
    }
}
