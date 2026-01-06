package com.MaudeLebeau.fridgecleaner.repository;

import com.MaudeLebeau.fridgecleaner.domain.Item;
import com.MaudeLebeau.fridgecleaner.domain.Product;
import com.MaudeLebeau.fridgecleaner.domain.Unit;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ItemRepository {

    private final ProductRepository productRepository;

    public ItemRepository(ProductRepository productRepository) { this.productRepository = productRepository; }

    public Item addItem(Item item) {
        String sql = """
                INSERT INTO items (product_id, quantity, unit, expiry_date, creation_date)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt =
                    conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, item.getProduct().getId());

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
                            item.getProduct(),
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
                SELECT id, product_id, quantity, unit, expiry_date, creation_date
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

                Date expirySql = rs.getDate("expiry_date");
                LocalDate expiryDate = (expirySql != null) ? expirySql.toLocalDate() : null;

                Product product = productRepository.getProductById(rs.getLong("product_id"));

                return new Item(
                        rs.getLong("id"),
                        product,
                        rs.getBigDecimal("quantity"),
                        Unit.valueOf(rs.getString("unit")),
                        expiryDate,
                        rs.getTimestamp("creation_date").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting item by id", e);
        }
    }

    public Item getItemByProductId(Long id) {
        String sql = """
                SELECT id, product_id, quantity, unit, expiry_date, creation_date
                FROM items
                WHERE product_id = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("Connection made");

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Date expirySql = rs.getDate("expiry_date");
                LocalDate expiryDate = (expirySql != null) ? expirySql.toLocalDate() : null;

                Product product = productRepository.getProductById(id);

                return new Item(
                        rs.getLong("id"),
                        product,
                        rs.getBigDecimal("quantity"),
                        Unit.valueOf(rs.getString("unit")),
                        expiryDate,
                        rs.getTimestamp("creation_date").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            System.out.println("No ResultSet");
            throw new RuntimeException("Error getting item by name", e);
        }
    }

    public List<Item> getAllItems() {
        String sql = """
                SELECT id, product_id, quantity, unit, expiry_date, creation_date
                FROM items
                """;

        List<Item> items = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {

                Date expirySql = rs.getDate("expiry_date");
                LocalDate expiryDate = (expirySql != null) ? expirySql.toLocalDate() : null;

                Product product = productRepository.getProductById(rs.getLong("product_id"));

                Item item = new Item(
                        rs.getLong("id"),
                        product,
                        rs.getBigDecimal("quantity"),
                        Unit.valueOf(rs.getString("unit")),
                        expiryDate
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
                SET quantity=?, unit=?, expiry_date=?
                WHERE id=?
                """;

        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, item.getQuantity());

            stmt.setString(2, item.getUnit().name());

            if (item.getExpiryDate() != null) {
                stmt.setDate(3, Date.valueOf(item.getExpiryDate()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            if (item.getId() != null) {
                stmt.setLong(4, item.getId());
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
