package com.MaudeLebeau.fridgecleaner.repository;

import com.MaudeLebeau.fridgecleaner.domain.Item;
import com.MaudeLebeau.fridgecleaner.domain.Unit;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
                    throw new SQLException("Failed to retrieve item generated ID");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting item", e);
        }
    }

    public Item getItemById(Long id) {
        String sql = """
                SELECT id, name, quantity, unit, expiry_date, creation_date
                FROM items
                WHERE id = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return new Item(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("quantity"),
                        Unit.valueOf(rs.getString("unit")),
                        rs.getString("expiry_date") != null
                                ? LocalDate.parse(rs.getString("expiry_date"))
                                : null,
                        rs.getTimestamp("creation_date").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting item by id", e);
        }
    }

    public List<Item> getAllItems() {
        String sql = """
                SELECT id, name, quantity, unit, expiry_date, creation_date
                FROM items
                """;

        List<Item> items = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Item item = new Item(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("quantity"),
                        Unit.valueOf(rs.getString("unit")),
                        rs.getString("expiry_date") != null
                            ? LocalDate.parse(rs.getString("expiry_date"))
                            : null
                );

                items.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching items", e);
        }
        return items;
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
