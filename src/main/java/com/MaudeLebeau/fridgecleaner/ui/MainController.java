package com.MaudeLebeau.fridgecleaner.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {

    @FXML
    private Label statusLabel;

    @FXML
    private void onInventory() {
        statusLabel.setText("Inventaire (a faire)");
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
