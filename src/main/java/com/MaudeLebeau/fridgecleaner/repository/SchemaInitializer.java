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
                    expiryDate DATE NOT NULL,
                    creationDate DATETIME NOT NULL,
                    )
                    """);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
