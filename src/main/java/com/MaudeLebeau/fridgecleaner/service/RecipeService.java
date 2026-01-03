package com.MaudeLebeau.fridgecleaner.service;

import com.MaudeLebeau.fridgecleaner.domain.Recipe;
import com.MaudeLebeau.fridgecleaner.repository.RecipeRepository;

public class RecipeService {
    private final RecipeRepository repo;

    public RecipeService(RecipeRepository repo) { this.repo = repo; }

    public Recipe addRecipe(Recipe recipe) {
        return repo.addRecipe(recipe);
    }
}
