package com.MaudeLebeau.fridgecleaner.service;

import com.MaudeLebeau.fridgecleaner.domain.Recipe;
import com.MaudeLebeau.fridgecleaner.repository.RecipeRepository;

import java.util.List;

public class RecipeService {
    private final RecipeRepository repo;

    public RecipeService(RecipeRepository repo) { this.repo = repo; }

    public Recipe addRecipe(Recipe recipe) {
        return repo.addRecipe(recipe);
    }

    public List<Recipe> listAll() { return repo.getAllRecipes(); }

    public Boolean deleteRecipe(Long id) {
        return repo.deleteRecipeById(id);
    }
}
