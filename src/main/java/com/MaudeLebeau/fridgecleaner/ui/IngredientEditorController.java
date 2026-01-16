package com.MaudeLebeau.fridgecleaner.ui;

import com.MaudeLebeau.fridgecleaner.domain.Ingredient;
import com.MaudeLebeau.fridgecleaner.domain.Product;
import com.MaudeLebeau.fridgecleaner.domain.Unit;
import com.MaudeLebeau.fridgecleaner.repository.ProductRepository;
import com.MaudeLebeau.fridgecleaner.service.ProductService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class IngredientEditorController {
    @FXML private VBox ingredientsBox;

    private final ProductService productService = new ProductService(new ProductRepository());

    public void initialize() {
        addIngredientRow();
    }

    public List<Ingredient> collectIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();

        for (Node node : ingredientsBox.getChildren()) {
            HBox row = (HBox) node;

            TextField nameField = (TextField) row.getChildren().get(0);
            TextField quantityField = (TextField)
                    row.getChildren().get(1);
            @SuppressWarnings("unchecked")
            ComboBox<Unit> unitBox = (ComboBox<Unit>)
                    row.getChildren().get(2);

            String name = nameField.getText().trim();
            String quantityRaw = quantityField.getText().trim();
            Unit unit = unitBox.getValue();

            if (name.isEmpty() && quantityRaw.isEmpty() && unit == null)
                continue;

            if (name.isEmpty() || quantityRaw.isEmpty() || unit == null)
                continue;

            BigDecimal quantity = new BigDecimal(quantityRaw);

            Product product = productService.getProductByName(name);

            if (product == null) {
                Product newProduct = new Product(null,
                        name,
                        unit);
                product = productService.addProduct(newProduct);

            }

            ingredients.add(new Ingredient(
                    product,
                    quantity,
                    unit
            ));
        }

        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        ingredientsBox.getChildren().clear();

        if (ingredients == null || ingredients.isEmpty()) {
            addIngredientRow();
            return;
        }

        for (Ingredient ing : ingredients) {
            addIngredientRowPrefilled(
                    ing.getProduct().getName(),
                    ing.getQuantity().toString(),
                    ing.getUnit()
            );
        }
    }

    private void addIngredientRowPrefilled(String name, String quantity, Unit unit) {
        HBox row = buildIngredientRow();

        TextField nameField = (TextField) row.getChildren().get(0);
        TextField quantityField = (TextField) row.getChildren().get(1);
        @SuppressWarnings("unchecked")
                ComboBox<Unit> unitField = (ComboBox<Unit>) row.getChildren().get(2);

        nameField.setText(name);
        quantityField.setText(quantity);
        unitField.setValue(unit);

        ingredientsBox.getChildren().add(row);
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
        return nameField.getText() != null && !
                nameField.getText().trim().isEmpty();
    }

    private boolean rowIsLast(HBox row) {
        int lastIndex = ingredientsBox.getChildren().size() - 1;
        return lastIndex >= 0 &&
                ingredientsBox.getChildren().get(lastIndex) == row;
    }

    private void focusLastIngredientName() {
        int lastIndex = ingredientsBox.getChildren().size() - 1;
        if (lastIndex < 0) {
            return;
        }
        HBox lastRow = (HBox) ingredientsBox.getChildren().get(lastIndex);
        if (!lastRow.getChildren().isEmpty() &&
                lastRow.getChildren().get(0) instanceof TextField) {
            lastRow.getChildren().get(0).requestFocus();
        }
    }
}



