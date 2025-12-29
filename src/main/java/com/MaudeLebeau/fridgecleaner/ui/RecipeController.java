package com.MaudeLebeau.fridgecleaner.ui;

import com.MaudeLebeau.fridgecleaner.domain.Recipe;
import com.MaudeLebeau.fridgecleaner.repository.RecipeRepository;
import com.MaudeLebeau.fridgecleaner.service.RecipeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class RecipeController {

    @FXML private TableView<Recipe> recipesTable;
    @FXML private TableColumn<Recipe, String> nameCol;
    @FXML private TableColumn<Recipe, Object> servingsCol;

    private final RecipeService recipeService =
            new RecipeService(new RecipeRepository());

    private final ObservableList<Recipe> recipes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        servingsCol.setCellValueFactory(new PropertyValueFactory<>("servings"));
    }
}
