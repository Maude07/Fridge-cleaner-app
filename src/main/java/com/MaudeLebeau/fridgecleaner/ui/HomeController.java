package com.MaudeLebeau.fridgecleaner.ui;

import com.MaudeLebeau.fridgecleaner.domain.*;
import com.MaudeLebeau.fridgecleaner.repository.ItemRepository;
import com.MaudeLebeau.fridgecleaner.repository.ProductRepository;
import com.MaudeLebeau.fridgecleaner.repository.RecipeRepository;
import com.MaudeLebeau.fridgecleaner.service.ItemService;
import com.MaudeLebeau.fridgecleaner.service.ProductService;
import com.MaudeLebeau.fridgecleaner.service.RecipeService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    @FXML private IngredientEditorController ingredientsEditorController;

    private final ItemService itemService = new ItemService(new ItemRepository(new ProductRepository()));
    private final RecipeService recipeService = new RecipeService(new RecipeRepository());
    private final ProductService productService = new ProductService(new ProductRepository());


    @FXML
    public void initialize() {
        unitCombo.getItems().setAll(Unit.values());
    }

    @FXML
    private void onAddItem() {
        try {
            String name = itemNameField.getText();
            BigDecimal quantity = new BigDecimal(quantityField.getText().trim());
            Unit unit = unitCombo.getValue();
            LocalDate expiry = expiryPicker.getValue();

            Product product = productService.getProductByName(name);

            if (product == null) {
                Product newProduct = new Product(
                        null, name, unit);
                product = productService.addProduct(newProduct);
            }

            Item item = new Item(null, product, quantity, unit, expiry);

            Item saved = itemService.add(item);

            statusLabel.setText("Added: " + saved.getProduct().getName());

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

            List<Ingredient> ingredients = ingredientsEditorController.collectIngredients();

            Recipe recipe = new Recipe(
                    null,
                    name,
                    servings,
                    recipeInstructions,
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
}