package com.MaudeLebeau.fridgecleaner.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MainController {

    @FXML private Label statusLabel;
    @FXML private StackPane contentPane;


    @FXML
    public void initialize() {
        showHome();
    }

    @FXML
    private void onHome() {
        showHome();
    }

    @FXML
    private void showHome() {
        loadView("/com/MaudeLebeau/fridgecleaner/ui/HomeView.fxml", "Accueil");
    }

    @FXML
    private void onInventory() {
        loadView("/com/MaudeLebeau/fridgecleaner/ui/InventoryView.fxml", "Inventaire");
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

    private void loadView(String fxml, String title) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxml));
            contentPane.getChildren().setAll(view);
            statusLabel.setText(title);
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Erreur de chargement : " + title);
        }
    }
}
