package com.MaudeLebeau.fridgecleaner.ui;

import com.MaudeLebeau.fridgecleaner.domain.Item;
import com.MaudeLebeau.fridgecleaner.domain.Unit;
import com.MaudeLebeau.fridgecleaner.repository.ItemRepository;
import com.MaudeLebeau.fridgecleaner.service.ItemService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MainController {

    @FXML private TextField nameField;
    @FXML private TextField quantityField;
    @FXML private ComboBox<Unit> unitCombo;
    @FXML private DatePicker expiryPicker;
    @FXML private Label statusLabel;
    @FXML private StackPane contentPane;

    private final ItemService itemService = new ItemService(new ItemRepository());

    @FXML
    public void initialize() {
        unitCombo.getItems().setAll(Unit.values());
        unitCombo.getSelectionModel().select(Unit.PCS);
    }

    @FXML
    private void onAddItem() {
        try {
            String name = nameField.getText();
            BigDecimal quantity = new BigDecimal(quantityField.getText().trim());
            Unit unit = unitCombo.getValue();
            LocalDate expiry = expiryPicker.getValue();

            Item item = new Item(null, name, quantity, unit, expiry);

            Item saved = itemService.add(item);

            statusLabel.setText("Added: " + saved.getName());

            nameField.clear();
            quantityField.clear();
            expiryPicker.setValue(null);

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Erreur: " + e.getMessage());
        }
    }

    @FXML
    private void onInventory() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/MaudeLebeau/fridgecleaner/ui/InventoryView.fxml")
            );
            Parent view = loader.load();
            contentPane.getChildren().setAll(view);
            statusLabel.setText("Inventaire");
    } catch (Exception e) {
        e.printStackTrace();
        statusLabel.setText("Erreur de chargement d'inventaire ");
        }
    }

    @FXML
    private void onRecipes() {
            statusLabel.setText("Recettes (a faire)");
        }

    @FXML
    private void onPrices() {
        statusLabel.setText("Prix (a faire)");
    }

    @FXML
    private void onSettings() {
        statusLabel.setText("Parametres (a faire)");
    }

    @FXML
    private void onQuickAdd() {
        statusLabel.setText("Ajouter un item (a faire)");
    }

    @FXML
    private void onQuickImport() {
        statusLabel.setText("Importer recette (a faire)");
    }
}
