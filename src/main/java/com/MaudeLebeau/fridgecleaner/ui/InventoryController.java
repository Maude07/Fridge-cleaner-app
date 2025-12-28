package com.MaudeLebeau.fridgecleaner.ui;

import com.MaudeLebeau.fridgecleaner.domain.Item;
import com.MaudeLebeau.fridgecleaner.repository.ItemRepository;
import com.MaudeLebeau.fridgecleaner.service.ItemService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class InventoryController {

    @FXML private TableView<Item> itemsTable;
    @FXML private TableColumn<Item, String> nameCol;
    @FXML private TableColumn<Item, Object> quantityCol;
    @FXML private TableColumn<Item, Object> unitCol;
    @FXML private TableColumn<Item, Object> expiryCol;

    private final ItemService itemService =
            new ItemService(new ItemRepository());

    private final ObservableList<Item> items = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));
        expiryCol.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));

        itemsTable.setItems(items);

        refresh();
    }

    public void refresh() {
        items.setAll(itemService.listAll());
    }

}
