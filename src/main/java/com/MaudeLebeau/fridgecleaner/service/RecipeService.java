package com.MaudeLebeau.fridgecleaner.service;

import com.MaudeLebeau.fridgecleaner.domain.*;
import com.MaudeLebeau.fridgecleaner.repository.ItemRepository;
import com.MaudeLebeau.fridgecleaner.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeService {
    private final RecipeRepository repo;
    private final ItemRepository itemRepo;

    public RecipeService(RecipeRepository repo, ItemRepository itemRepo) {
        this.repo = repo;
        this.itemRepo = itemRepo;}

    public Recipe addRecipe(Recipe recipe) {
        return repo.addRecipe(recipe);
    }

    public List<Recipe> listAll() { return repo.getAllRecipes(); }

    public Boolean deleteRecipe(Long id) {
        return repo.deleteRecipeById(id);
    }

    public Boolean updateRecipe(Recipe recipe) {
        return repo.updateRecipe(recipe);
    }

    public List<RecipeMatchResult> listRankedRecipes() {

        List<Recipe> recipes = repo.getAllRecipes();
        List<Item> inventoryItems = itemRepo.getAllItems();

        List<RecipeMatchResult> results =
                rankRecipesByInventory(inventoryItems, recipes);

        return results;
    }

    public List<RecipeMatchResult> rankRecipesByInventory(List<Item> inventoryItems, List<Recipe> recipes) {

        Set<Product> inventoryProducts = inventoryItems.stream()
                .map(Item::getProduct)
                .collect(Collectors.toSet());

        List<RecipeMatchResult> matchResults = new ArrayList<>(recipes.size());

        for (Recipe recipe : recipes) {
            int missingCount = 0;
            List<Ingredient> missingIngredients = new ArrayList<>();

            for (Ingredient ingredient : recipe.getIngredientList()) {
                Product p = ingredient.getProduct();

                if (!inventoryProducts.contains(p)) {
                    missingCount++;
                    missingIngredients.add(ingredient);
                }
            }

            RecipeStatus status = computeStatus(missingCount, recipe.getIngredientList());

            matchResults.add(new RecipeMatchResult(recipe,
                    missingCount,
                    missingIngredients,
                    status));
        }
        return matchResults;
    }

    private RecipeStatus computeStatus(int missingCount, List<Ingredient> ingredientList) {
        if (missingCount == 0) {
            return RecipeStatus.OK;
        }

        double ratio = (double) missingCount / ingredientList.size();
        return (ratio <= 0.25) ? RecipeStatus.PARTIEL : RecipeStatus.NON;
    }
}


