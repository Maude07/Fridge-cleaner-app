package com.MaudeLebeau.fridgecleaner.ui;

import com.MaudeLebeau.fridgecleaner.domain.Item;
import com.MaudeLebeau.fridgecleaner.repository.ItemRepository;
import com.MaudeLebeau.fridgecleaner.service.ItemService;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.math.BigDecimal;

public class InventoryController {

    @FXML private TableView<Item> itemsTable;
    @FXML private TableColumn<Item, String> nameCol;
    @FXML private TableColumn<Item, Object> quantityCol;
    @FXML private TableColumn<Item, Object> unitCol;
    @FXML private TableColumn<Item, Object> expiryCol;
    @FXML private TableColumn<Item, Void> actionsCol;

    private final ItemService itemService =
            new ItemService(new ItemRepository());

    private final ObservableList<Item> items = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));
        expiryCol.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        actionsCol.setCellFactory(col -> new TableCell<>() {

            private final Button plusBtn = new Button("+");
            private final Button minusBtn = new Button("-");
            private final HBox box = new HBox(1, plusBtn, minusBtn);

            {
                plusBtn.setOnAction(e -> {
                    Item item = getTableRow().getItem();
                    if (item == null) {
                        return ;
                    }

                    showAddQuantityDialog(item);
                    refresh();
                });

                minusBtn.setOnAction(e -> {
                    Item item = getTableRow().getItem();
                    itemService.delete(item);
                    System.out.println("MINUS on " + item.getName());
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

        itemsTable.setItems(items);

        refresh();
    }

    public void refresh() {
        items.setAll(itemService.listAll());
    }

    private void showAddQuantityDialog(Item item) {
        //TODO when updating quantity, update expiry_date, but keep old items and new items separate?
        // ex: I have an old milk carton, but I just bought a new one, keep old expiry date until used all of it
        Dialog<BigDecimal> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une quantite");
        dialog.setHeaderText("Item: " + item.getName());

        ButtonType addBtnType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtnType, ButtonType.CANCEL);

        TextField addField = new TextField();
        addField.setPromptText("ex: 0.5");

        Label currentLabel = new Label( "Actuel: " + item.getQuantity() + " " + item.getUnit());
        Label previewLabel = new Label("Apres ajoute: -");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.addRow(0, new Label("Ajouter:"), addField);
        grid.add(currentLabel, 0, 1, 2, 1);
        grid.add(previewLabel, 0, 2, 2, 1);

        dialog.getDialogPane().setContent(grid);

        Node okButton = dialog.getDialogPane().lookupButton(addBtnType);

        BooleanBinding invalid = Bindings.createBooleanBinding(() -> {
            String txt = addField.getText() == null ? "" : addField.getText().trim();
            if (txt.isEmpty()) {
                previewLabel.setText("Apres ajout : -");
                return true;
            }
            try {
                BigDecimal add = new BigDecimal(txt);
                if (add.compareTo(BigDecimal.ZERO) <= 0) {
                    previewLabel.setText("Apres ajout : quantite invalide");
                    return true;
                }
                BigDecimal after = item.getQuantity().add(add);
                previewLabel.setText("Apres ajout: " + after + " " + item.getUnit());
                return false;
            } catch (NumberFormatException e) {
                previewLabel.setText("Apres ajout : format invalide");
                return true;
            }
        }, addField.textProperty());

        okButton.disableProperty().bind(invalid);

        dialog.setOnShown(evt -> addField.requestFocus());

        dialog.setResultConverter(btn -> {
            if (btn == addBtnType) {
                return new BigDecimal(addField.getText().trim());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(addQty -> {
            BigDecimal sum = addQty.add(item.getQuantity());
            itemService.update(new Item(
                    item.getId(),
                    item.getName(),
                    sum,
                    item.getUnit(),
                    item.getExpiryDate(),
                    item.getCreationDate()
            ));
        });
    }
}
