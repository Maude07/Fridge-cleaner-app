package com.MaudeLebeau.fridgecleaner.service;

import com.MaudeLebeau.fridgecleaner.domain.Product;
import com.MaudeLebeau.fridgecleaner.repository.ProductRepository;

import java.util.List;

public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo) { this.repo = repo;}

    public Product addProduct(Product product) {
        return repo.addProduct(product);
    }

    public Product getProductByName(String name) { return repo.getProductByName(name); }

    public Product getProductById(Long id) { return repo.getProductById(id); }
}
