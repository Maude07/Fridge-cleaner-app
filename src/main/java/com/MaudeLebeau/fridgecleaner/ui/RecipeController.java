package com.MaudeLebeau.fridgecleaner.ui;

import com.MaudeLebeau.fridgecleaner.domain.Recipe;
import com.MaudeLebeau.fridgecleaner.domain.RecipeMatchResult;
import com.MaudeLebeau.fridgecleaner.domain.RecipeStatus;
import com.MaudeLebeau.fridgecleaner.repository.ItemRepository;
import com.MaudeLebeau.fridgecleaner.repository.ProductRepository;
import com.MaudeLebeau.fridgecleaner.repository.RecipeRepository;
import com.MaudeLebeau.fridgecleaner.service.RecipeService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class RecipeController {

    @FXML private TableView<RecipeMatchResult> recipesTable;

    @FXML private TableColumn<RecipeMatchResult, RecipeStatus> statusCol;
    @FXML private TableColumn<RecipeMatchResult, String> nameCol;
    @FXML private TableColumn<RecipeMatchResult, Object> servingsCol;
    @FXML private TableColumn<RecipeMatchResult, Integer> missingCol;
    @FXML private TableColumn<RecipeMatchResult, Void> actionsCol;

    private final RecipeService recipeService =
            new RecipeService(new RecipeRepository(), new ItemRepository(new ProductRepository()));

    private final ObservableList<RecipeMatchResult> recipes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getRecipe().getName()));
        servingsCol.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getRecipe().getServings()));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        missingCol.setCellValueFactory(new PropertyValueFactory<>("missing"));
        actionsCol.setCellFactory(col -> new TableCell<>() {

            private final Button plusBtn = new Button("+");
            private final Button minusBtn = new Button("-");
            private final HBox box = new HBox(1, plusBtn, minusBtn);

            {
                plusBtn.setOnAction(e -> {

                    Recipe recipe = getTableRow().getItem().getRecipe();
                    if (recipe == null) {
                        return;
                    }
                    openRecipeEditor(recipe);
                });

                minusBtn.setOnAction(e -> {
                    Recipe recipe = getTableRow().getItem().getRecipe();
                    recipeService.deleteRecipe(recipe.getId());
                    System.out.println("MINUS on " + recipe.getName());
                    refresh();
                });
            }

            @Override
            protected void updateItem(Void ignored, boolean empty) {
                super.updateItem(ignored, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(box);
                }
            }
        });

        recipesTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(RecipeMatchResult item, boolean empty) {
                super.updateItem(item, empty);

                getStyleClass().removeAll("recipe-ready", "recipe-almost", "recipe-unavailable");

                if (empty || item == null) return;

                switch (item.getStatus()) {
                    case OK -> getStyleClass().add("recipe-ready");
                    case PARTIEL -> getStyleClass().add("recipe-almost");
                    case NON -> getStyleClass().add("recipe-unavailable");
                }
            }
        });

        recipesTable.setItems(recipes);
        refresh();
    }

    public void refresh() { recipes.setAll(recipeService.listRankedRecipes()); }

    private void openRecipeEditor(Recipe recipe) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/MaudeLebeau/fridgecleaner/ui/RecipeEditorPane.fxml"));

            Parent root = loader.load();

            RecipeEditorController controller = loader.getController();
            controller.setRecipe(recipe);

            Stage stage = new Stage();
            stage.setTitle("Modificateur de recette");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
