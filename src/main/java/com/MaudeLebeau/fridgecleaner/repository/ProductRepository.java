package com.MaudeLebeau.fridgecleaner.repository;

import com.MaudeLebeau.fridgecleaner.domain.Product;
import com.MaudeLebeau.fridgecleaner.domain.Unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRepository {

    public Product addProduct(Product product) {
        String sql = """
                INSERT INTO products (name, default_unit) VALUES (?, ?)
                """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

             stmt.setString(1, product.getName());

             stmt.setString(2, product.getUnit().name());

             stmt.executeUpdate();

             try (ResultSet keys = stmt.getGeneratedKeys()) {
                 if (keys.next()) {
                     Long generatedId = keys.getLong(1);

                     return new Product(
                             generatedId,
                             product.getName(),
                             product.getUnit()
                     );
                 } else {
                     throw new SQLException("Failed to retrieve product generated ID");
                 }
             }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting product", e);
        }
    }

    public Product getProductById(Long id) {
        String sql = """
                SELECT id, name, default_unit 
                FROM products WHERE id = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        Unit.valueOf(rs.getString("default_unit"))
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting product by id", e);
        }
    }

    public Product getProductByName(String name) {
        String sql = """
                SELECT id, name, default_unit
                FROM products WHERE name = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);

            try (ResultSet rs= stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return new Product(
                    rs.getLong("id"),
                    rs.getString("name"),
                    Unit.valueOf(rs.getString("default_unit"))
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting product by name", e);
        }
    }
}
