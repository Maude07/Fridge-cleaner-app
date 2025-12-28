package com.MaudeLebeau.fridgecleaner.service;

import com.MaudeLebeau.fridgecleaner.domain.Item;
import com.MaudeLebeau.fridgecleaner.repository.ItemRepository;

public class ItemService {
    private final ItemRepository repo;

    public ItemService(ItemRepository repo) {
        this.repo = repo;
    }

    public Item add(Item item) {
        //TODO ajouter verifications si necessaire
        return repo.addItem(item);
    }
}
