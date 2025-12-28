package com.MaudeLebeau.fridgecleaner.ui;

import com.MaudeLebeau.fridgecleaner.domain.Item;
import com.MaudeLebeau.fridgecleaner.repository.ItemRepository;
import com.MaudeLebeau.fridgecleaner.service.ItemService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

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
                    Item item = getTableView().getItems().get(getIndex());
                    //TODO increment item
                    System.out.println("PLUS on " + item.getName());
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

}
