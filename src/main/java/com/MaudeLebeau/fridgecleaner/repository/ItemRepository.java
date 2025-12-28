package com.MaudeLebeau.fridgecleaner.repository;

import com.MaudeLebeau.fridgecleaner.domain.Item;

import java.sql.*;

public class ItemRepository {

    public Item addItem(Item item) {
        String sql = """
                INSERT INTO items (name, quantity, unit, expiry_date, creation_date)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt =
                    conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, item.getName());

            stmt.setBigDecimal(2, item.getQuantity());

            stmt.setString(3, item.getUnit().name());

            if (item.getExpiryDate() != null) {
                stmt.setDate(4, Date.valueOf(item.getExpiryDate()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.setTimestamp(5, Timestamp.valueOf(item.getCreationDate()));

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    Long generatedId = keys.getLong(1);

                    return new Item(
                            generatedId,
                            item.getName(),
                            item.getQuantity(),
                            item.getUnit(),
                            item.getExpiryDate()
                    );
                } else {
                    throw new SQLException("Failed to retrieve generated ID");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting item", e);
        }
    }

    public boolean updateItem(Item item) {

        if (item.getId() == null) {
            throw new IllegalArgumentException("Cannot update item without Id");
        }

        String sql = """
                UPDATE items
                SET name=?, quantity=?, unit=?, expiry_date=?
                WHERE id=?
                """;

        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getName());

            stmt.setBigDecimal(2, item.getQuantity());

            stmt.setString(3, item.getUnit().name());

            if (item.getExpiryDate() != null) {
                stmt.setDate(4, Date.valueOf(item.getExpiryDate()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            if (item.getId() != null) {
                stmt.setLong(5, item.getId());
            }

            int rows = stmt.executeUpdate();
            return rows == 1;

        } catch (Exception e) {
            throw new RuntimeException("Error updating item id = " + item.getId(), e);
        }
    }

    public boolean deleteItemById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Cannot delete item without Id");
        }

        String sql = """
                DELETE FROM items WHERE id = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            int rows = stmt.executeUpdate();
            return rows == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting item id = " + id, e);
        }
    }
}
