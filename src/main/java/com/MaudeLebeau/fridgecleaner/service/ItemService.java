package com.MaudeLebeau.fridgecleaner.service;

import com.MaudeLebeau.fridgecleaner.domain.Item;
import com.MaudeLebeau.fridgecleaner.repository.ItemRepository;

import java.util.List;

public class ItemService {
    private final ItemRepository repo;

    public ItemService(ItemRepository repo) {
        this.repo = repo;
    }

    public Item add(Item item) {
        //TODO ajouter verifications si necessaire
        return repo.addItem(item);
    }

    public void delete(Item item) {
        repo.deleteItemById(item.getId());
    }


    public List<Item> listAll() {
        return repo.getAllItems();
    }
}
