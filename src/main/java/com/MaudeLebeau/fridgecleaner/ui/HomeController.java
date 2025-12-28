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
import javafx.scene.layout.StackPane;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HomeController {

    @FXML private TextField nameField;
    @FXML private TextField quantityField;
    @FXML private ComboBox<Unit> unitCombo;
    @FXML private DatePicker expiryPicker;
    @FXML private Label statusLabel;

    private final ItemService itemService = new ItemService(new ItemRepository());


    @FXML
    public void initialize() {
        unitCombo.getItems().setAll(Unit.values());
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
    private void onQuickImport() {
        statusLabel.setText("Quick import");
    }
}
