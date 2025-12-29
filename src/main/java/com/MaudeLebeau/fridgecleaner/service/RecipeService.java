package com.MaudeLebeau.fridgecleaner.service;

import com.MaudeLebeau.fridgecleaner.repository.RecipeRepository;

public class RecipeService {
    private final RecipeRepository repo;

    public RecipeService(RecipeRepository repo) { this.repo = repo; }
}
