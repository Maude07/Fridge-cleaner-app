package com.MaudeLebeau.fridgecleaner.ui;

import com.MaudeLebeau.fridgecleaner.domain.Ingredient;
import com.MaudeLebeau.fridgecleaner.domain.Item;
import com.MaudeLebeau.fridgecleaner.domain.Recipe;
import com.MaudeLebeau.fridgecleaner.domain.Unit;
import com.MaudeLebeau.fridgecleaner.repository.ItemRepository;
import com.MaudeLebeau.fridgecleaner.repository.RecipeRepository;
import com.MaudeLebeau.fridgecleaner.service.ItemService;
import com.MaudeLebeau.fridgecleaner.service.RecipeService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HomeController {

    @FXML private TextField itemNameField;
    @FXML private TextField quantityField;
    @FXML private ComboBox<Unit> unitCombo;
    @FXML private DatePicker expiryPicker;
    @FXML private TextField recipeNameField;
    @FXML private TextField servingsField;
    @FXML private TextArea recipeInstructionsField;
    @FXML private Label statusLabel;
    @FXML private VBox ingredientsBox;

    private final ItemService itemService = new ItemService(new ItemRepository());
    private final RecipeService recipeService = new RecipeService(new RecipeRepository());

    @FXML
    public void initialize() {
        unitCombo.getItems().setAll(Unit.values());
        addIngredientRow();
    }

    public List<Ingredient> collectIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();

        for (Node node : ingredientsBox.getChildren()) {
            HBox row = (HBox) node;

            TextField nameField = (TextField) row.getChildren().get(0);
            TextField quantityField = (TextField) row.getChildren().get(1);
            @SuppressWarnings("unchecked")
            ComboBox<Unit> unitBox = (ComboBox<Unit>) row.getChildren().get(2);

            String name = nameField.getText().trim();
            String quantityRaw = quantityField.getText().trim();
            Unit unit = unitBox.getValue();

            if (name.isEmpty() && quantityRaw.isEmpty() && unit == null) continue;

            //TODO generer une erreur UI si les entrees sont partielles
            if (name.isEmpty() || quantityRaw.isEmpty() || unit == null) continue;

            BigDecimal quantity = new BigDecimal(quantityRaw);
            Long ingredientId = itemService.findItemByName(name).getId();

            ingredients.add(new Ingredient(
                    ingredientId,
                    name,
                    quantity,
                    unit
            ));
        }

        return ingredients;
    }

    @FXML
    private void onAddItem() {
        try {
            String name = itemNameField.getText();
            BigDecimal quantity = new BigDecimal(quantityField.getText().trim());
            Unit unit = unitCombo.getValue();
            LocalDate expiry = expiryPicker.getValue();

            Item item = new Item(null, name, quantity, unit, expiry);

            Item saved = itemService.add(item);

            statusLabel.setText("Added: " + saved.getName());

            itemNameField.clear();
            quantityField.clear();
            expiryPicker.setValue(null);

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Erreur: " + e.getMessage());
        }
    }

    @FXML
    private void onAddRecipe() {
        try {
            String name = recipeNameField.getText();
            Integer servings = Integer.parseInt(servingsField.getText());
            String recipeInstructions = recipeInstructionsField.getText();

            List<Ingredient> ingredients = collectIngredients();

            Recipe recipe = new Recipe(
                    null,
                    name,
                    recipeInstructions,
                    servings,
                    ingredients
            );

            Recipe saved = recipeService.addRecipe(recipe);

            statusLabel.setText("Added: " + saved.getName());

            recipeNameField.clear();
            servingsField.clear();
            recipeInstructionsField.clear();

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Erreur: " + e.getMessage());
        }
    }

    private void addIngredientRow() {
        HBox row = buildIngredientRow();
        ingredientsBox.getChildren().add(row);
        row.requestFocus();
    }

    private HBox buildIngredientRow() {
        TextField nameField = new TextField();
        nameField.setPromptText("Ingredient");

        TextField qtyField = new TextField();
        qtyField.setPromptText("Quantite");

        ComboBox<Unit> unitField = new ComboBox<>();
        unitField.getItems().setAll(Unit.values());
        unitField.setPromptText("Unite");

        HBox row = new HBox(10, nameField, qtyField, unitField);

        nameField.setOnAction(event -> maybeAppendRow(row, nameField));
        qtyField.setOnAction(event -> maybeAppendRow(row, nameField));
        unitField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                maybeAppendRow(row, nameField);
            }
        });

        return row;
    }

    private void maybeAppendRow(HBox row, TextField nameField) {
        if (!rowIsLast(row) || !rowHasName(nameField)) {
            return;
        }
        addIngredientRow();
        focusLastIngredientName();
    }

    private boolean rowHasName(TextField nameField) {
        return nameField.getText() != null && !nameField.getText().trim().isEmpty();
    }

    private boolean rowIsLast(HBox row) {
        int lastIndex = ingredientsBox.getChildren().size() - 1;
        return lastIndex >= 0 && ingredientsBox.getChildren().get(lastIndex) == row;
    }

    private void focusLastIngredientName() {
        int lastIndex = ingredientsBox.getChildren().size() - 1;
        if (lastIndex < 0) {
            return;
        }
        HBox lastRow = (HBox) ingredientsBox.getChildren().get(lastIndex);
        if (!lastRow.getChildren().isEmpty() && lastRow.getChildren().get(0) instanceof TextField) {
            lastRow.getChildren().get(0).requestFocus();
        }
    }
}