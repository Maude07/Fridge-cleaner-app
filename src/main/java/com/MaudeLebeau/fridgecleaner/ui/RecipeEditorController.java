package com.MaudeLebeau.fridgecleaner.ui;

import com.MaudeLebeau.fridgecleaner.domain.Ingredient;
import com.MaudeLebeau.fridgecleaner.domain.Recipe;
import com.MaudeLebeau.fridgecleaner.repository.ItemRepository;
import com.MaudeLebeau.fridgecleaner.repository.ProductRepository;
import com.MaudeLebeau.fridgecleaner.repository.RecipeRepository;
import com.MaudeLebeau.fridgecleaner.service.RecipeService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class RecipeEditorController {
    @FXML private TextField nameField;
    @FXML private TextField servingsField;
    @FXML private TextArea instructionsField;

    @FXML private VBox ingredientsEditor;
    @FXML private IngredientEditorController ingredientsEditorController;

    private final RecipeService recipeService = new RecipeService(new RecipeRepository(), new ItemRepository(new ProductRepository()));
    private Recipe editingRecipe;

    public void initialize() {
    }

    public void setRecipe(Recipe recipe) {
        this.editingRecipe = recipe;

        nameField.setText(recipe.getName());
        servingsField.setText(String.valueOf(recipe.getServings()));
        instructionsField.setText(recipe.getInstructions());

        if (ingredientsEditorController != null) {
            ingredientsEditorController.setIngredients(recipe.getIngredientList());
        }
    }

    @FXML private void onSave() {
        try {
            String name = nameField.getText();
            Integer servings = Integer.parseInt(servingsField.getText());
            String recipeInstructions = instructionsField.getText();

            List<Ingredient> ingredients = ingredientsEditorController.collectIngredients();

            if (editingRecipe != null) {
                Recipe updatedRecipe = new Recipe(editingRecipe.getId(),
                        name, servings, recipeInstructions, ingredients);

                Boolean updated = recipeService.updateRecipe(updatedRecipe);
                if (updated) {
                    Stage stage = (Stage) nameField.getScene().getWindow();
                    stage.close();
                } else {
                    System.out.println("Failed to update recipe" + name);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML private void onCancel() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }


}
