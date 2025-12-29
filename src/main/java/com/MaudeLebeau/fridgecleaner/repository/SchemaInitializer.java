package com.MaudeLebeau.fridgecleaner.repository;

import java.sql.Connection;
import java.sql.Statement;

public class SchemaInitializer {

    public static void init() {
        try (Connection conn = DatabaseManager.getConnection();

             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS items (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    quantity REAL NOT NULL,
                    unit TEXT NOT NULL,
                    expiry_date DATE,
                    creation_date DATETIME NOT NULL
                    );
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS recipes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    servings INTEGER NOT NULL CHECK (servings > 0)
                    );
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS recipe_ingredients (
                    recipe_id INTEGER NOT NULL,
                    item_id INTEGER NOT NULL,
                    quantity REAL NOT NULL CHECK (quantity > 0),
                    unit TEXT NOT NULL,
                    
                    PRIMARY KEY (recipe_id, item_id),
                    FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
                    FOREIGN KEY (item_id) REFERENCES items(id)
                    );
                    """);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
