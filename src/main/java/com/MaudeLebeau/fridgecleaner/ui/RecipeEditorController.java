package com.MaudeLebeau.fridgecleaner.ui;

import com.MaudeLebeau.fridgecleaner.domain.Ingredient;
import com.MaudeLebeau.fridgecleaner.domain.Recipe;
import com.MaudeLebeau.fridgecleaner.repository.RecipeRepository;
import com.MaudeLebeau.fridgecleaner.service.ItemService;
import com.MaudeLebeau.fridgecleaner.service.RecipeService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class RecipeEditorController {
    @FXML private TextField nameField;
    @FXML private TextField servingsField;
    @FXML private TextArea instructionsField;

    @FXML private VBox ingredientsEditor;
    @FXML private IngredientEditorController ingredientsEditorController;

    private final RecipeService recipeService = new RecipeService(new RecipeRepository());
    private Recipe editingRecipe;

    public void initialize() {
    }

    public void setRecipe(Recipe recipe) {
        this.editingRecipe = recipe;

        nameField.setText(recipe.getName());
        servingsField.setText(String.valueOf(recipe.getServings()));
        instructionsField.setText(recipe.getInstructions());

    }

    @FXML private void onSave() {

        try {
            String name = nameField.getText();
            Integer servings = Integer.parseInt(servingsField.getText());
            String recipeInstructions = instructionsField.getText();

            List<Ingredient> ingredients = ingredientsEditorController.collectIngredients();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML private void onCancel() {

    }


}
