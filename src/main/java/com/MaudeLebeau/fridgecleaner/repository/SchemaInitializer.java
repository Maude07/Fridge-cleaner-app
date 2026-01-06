package com.MaudeLebeau.fridgecleaner.repository;

import java.sql.Connection;
import java.sql.Statement;

public class SchemaInitializer {

    public static void init() {
        try (Connection conn = DatabaseManager.getConnection();

             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS products (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL UNIQUE,
                        default_unit TEXT
                    );
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS items (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    product_id INTEGER NOT NULL,
                    quantity REAL NOT NULL CHECK (quantity > 0),
                    unit TEXT NOT NULL,
                    expiry_date DATE,
                    creation_date DATETIME NOT NULL,
                    FOREIGN KEY (product_id) REFERENCES products(id)
                    );
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS recipes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    servings INTEGER NOT NULL CHECK (servings > 0),
                    instructions TEXT
                    );
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS recipe_ingredients (
                    recipe_id INTEGER NOT NULL,
                    product_id INTEGER NOT NULL,
                    quantity REAL NOT NULL CHECK (quantity > 0),
                    unit TEXT NOT NULL,
                    
                    PRIMARY KEY (recipe_id, product_id),
                    FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
                    FOREIGN KEY (product_id) REFERENCES products(id)
                    );
                    """);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
