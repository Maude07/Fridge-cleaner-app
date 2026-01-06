package com.MaudeLebeau.fridgecleaner.ui;

import com.MaudeLebeau.fridgecleaner.domain.Recipe;
import com.MaudeLebeau.fridgecleaner.repository.RecipeRepository;
import com.MaudeLebeau.fridgecleaner.service.RecipeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class RecipeController {

    @FXML private TableView<Recipe> recipesTable;
    @FXML private TableColumn<Recipe, String> nameCol;
    @FXML private TableColumn<Recipe, Object> servingsCol;
    @FXML private TableColumn<Recipe, Void> actionsCol;

    private final RecipeService recipeService =
            new RecipeService(new RecipeRepository());

    private final ObservableList<Recipe> recipes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        servingsCol.setCellValueFactory(new PropertyValueFactory<>("servings"));
        actionsCol.setCellFactory(col -> new TableCell<>() {

            private final Button plusBtn = new Button("+");
            private final Button minusBtn = new Button("-");
            private final HBox box = new HBox(1, plusBtn, minusBtn);

            {
                plusBtn.setOnAction(e -> {

                    Recipe recipe = getTableRow().getItem();
                    if (recipe == null) {
                        return;
                    }
                    refresh();
                });

                minusBtn.setOnAction(e -> {
                    Recipe recipe = getTableRow().getItem();
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

        recipesTable.setItems(recipes);
        refresh();
    }

    public void refresh() { recipes.setAll(recipeService.listAll());}

//    private void openRecipeEditor() {
//        try {
//            FXMLLoader loader = new FXMLLoader(
//                    getClass().getResource(
//                            "/com/MaudeLebeau/fridgecleaner/ui/RecipeEditorPane.fxml"));
//            Parent root = loader.load();
//            Stage stage = new Stage();
//            stage.setTitle("Modificateur de recette");
//            stage.setScene(new Scene(root))
//        }
//    }
}
