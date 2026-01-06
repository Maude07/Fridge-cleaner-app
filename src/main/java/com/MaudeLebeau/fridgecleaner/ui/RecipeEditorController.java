package com.MaudeLebeau.fridgecleaner.ui;

import com.MaudeLebeau.fridgecleaner.domain.Ingredient;
import com.MaudeLebeau.fridgecleaner.domain.Unit;
import com.MaudeLebeau.fridgecleaner.repository.ItemRepository;
import com.MaudeLebeau.fridgecleaner.repository.ProductRepository;
import com.MaudeLebeau.fridgecleaner.repository.RecipeRepository;
import com.MaudeLebeau.fridgecleaner.service.ItemService;
import com.MaudeLebeau.fridgecleaner.service.RecipeService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RecipeEditorController {
    @FXML private TextField nameField;
    @FXML private TextField servingsField;
    @FXML private TextArea instructionsField;
    @FXML private VBox ingredientsBox;
    @FXML private IngredientEditorController ingredientsEditor;

    private final ItemService itemService = new ItemService(new ItemRepository(new ProductRepository()));
    private final RecipeService recipeService = new RecipeService(new RecipeRepository());

    public void initialize() {
    }


    @FXML private void onSave() {

        try {
            String name = nameField.getText();
            Integer servings = Integer.parseInt(servingsField.getText());
            String recipeInstructions = instructionsField.getText();

            List<Ingredient> ingredients = ingredientsEditor.collectIngredients();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML private void onCancel() {

    }


}
