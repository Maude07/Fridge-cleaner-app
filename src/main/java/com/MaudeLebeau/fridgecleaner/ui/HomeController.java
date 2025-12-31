package com.MaudeLebeau.fridgecleaner.ui;

import com.MaudeLebeau.fridgecleaner.domain.Item;
import com.MaudeLebeau.fridgecleaner.domain.Unit;
import com.MaudeLebeau.fridgecleaner.repository.ItemRepository;
import com.MaudeLebeau.fridgecleaner.service.ItemService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HomeController {

    @FXML private TextField itemNameField;
    @FXML private TextField quantityField;
    @FXML private ComboBox<Unit> unitCombo;
    @FXML private DatePicker expiryPicker;
    @FXML private TextField recipeNameField;
    @FXML private TextField servingsField;
    @FXML private Label statusLabel;
    @FXML private VBox ingredientsBox;

    private final ItemService itemService = new ItemService(new ItemRepository());


    @FXML
    public void initialize() {
        unitCombo.getItems().setAll(Unit.values());
        addIngredientRow();
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
        String name = recipeNameField.getText();
        Integer servings = Integer.parseInt(servingsField.getText());

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