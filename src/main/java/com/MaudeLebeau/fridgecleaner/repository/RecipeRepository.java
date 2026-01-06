package com.MaudeLebeau.fridgecleaner.repository;

import com.MaudeLebeau.fridgecleaner.domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RecipeRepository {

    ProductRepository productRepository;
    ItemRepository itemRepository;

    public Recipe addRecipe(Recipe recipe) {
        String recipeSql = """
                INSERT INTO recipes (name, servings, instructions)
                VALUES (?, ?, ?)
                """;

        String ingredientSql = """
                INSERT INTO recipe_ingredients(recipe_id, product_id, quantity, unit)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);

            try {
                long recipeId;

                try (PreparedStatement stmt = conn.prepareStatement(recipeSql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, recipe.getName());
                    stmt.setInt(2, recipe.getServings());
                    stmt.setString(3, recipe.getInstructions());
                    stmt.executeUpdate();

                    try (ResultSet keys = stmt.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Failed to retrieve recipe generated ID");
                        }
                        recipeId = keys.getLong(1);
                    }
                }

                if (recipe.getIngredientList() != null &&  !recipe.getIngredientList().isEmpty()) {
                    try (PreparedStatement stmt = conn.prepareStatement(ingredientSql)) {
                        for (Ingredient ing : recipe.getIngredientList()) {
                            stmt.setLong(1, recipeId);
                            stmt.setLong(2, ing.getProduct().getId());
                            stmt.setBigDecimal(3, ing.getQuantity());
                            stmt.setString(4, ing.getUnit().toString());
                            stmt.addBatch();
                        }
                        stmt.executeBatch();
                    }
                }

                conn.commit();

                return new Recipe(recipeId, recipe.getName(), recipe.getServings(), recipe.getInstructions(), recipe.getIngredientList());

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting recipe", e);
        }
    }

    public Recipe getRecipeById(Long id) {
        String recipeSql = """
                SELECT id, name, servings, instructions FROM recipes WHERE id = ?
                """;

        String ingredientsSql = """
                SELECT p.id AS product_id,
                       p.name AS product_name,
                       ri.quantity, ri.unit
                FROM recipe_ingredients ri
                JOIN products p ON p.id = ri.product_id
                WHERE ri.recipe_id = ?
                ORDER BY p.name
                """;

        try (Connection conn = DatabaseManager.getConnection()) {

            Recipe baseRecipe;
            try (PreparedStatement stmt = conn.prepareStatement(recipeSql)) {
                stmt.setLong(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) return null;

                    baseRecipe = new Recipe(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getInt("servings"),
                            rs.getString("instructions"),
                            new ArrayList<>()
                    );
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(ingredientsSql)) {
                stmt.setLong(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {

                            Product product = productRepository.getProductById(rs.getLong("product_id"));

                        Ingredient ing = new Ingredient(
                                product,
                                rs.getBigDecimal("quantity"),
                                Unit.valueOf(rs.getString("unit"))
                        );
                        baseRecipe.getIngredientList().add(ing);
                    }
                }
            }

            return baseRecipe;

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting ingredient", e);
        }
    }

    public List<Recipe> getAllRecipes() {
        String sql = """
                SELECT r.id, r.name, r.servings, r.instructions,
                    p.id AS product_id,
                    p.name AS product_name,
                    p.default_unit AS product_default_unit,
                    ri.quantity, ri.unit
                FROM recipes r
                LEFT JOIN recipe_ingredients ri ON ri.recipe_id = r.id
                LEFT JOIN products p ON p.id = ri.product_id
                ORDER BY r.id, p.name
                """;

        Map<Long, Recipe> byId = new LinkedHashMap<>();

        try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                long recipeId = rs.getLong("id");

                Recipe recipe = byId.get(recipeId);
                if (recipe == null) {
                    recipe = new Recipe(
                            recipeId,
                            rs.getString("name"),
                            rs.getInt("servings"),
                            rs.getString("instructions"),
                            new ArrayList<>()
                    );
                    byId.put(recipeId, recipe);
                }

                Object rawProductId = rs.getObject("product_id");
                Long productId = (rawProductId == null) ? null : ((Number) rawProductId).longValue();

                if (productId != null) {
                    Product product = new Product(
                            productId,
                            rs.getString("product_name"),
                            rs.getString("product_default_unit") == null
                                ? null
                                : Unit.valueOf(rs.getString("product_default_unit"))
                    );

                    Ingredient ing = new Ingredient(
                            product,
                            rs.getBigDecimal("quantity"),
                            Unit.valueOf(rs.getString("unit"))
                    );
                    recipe.getIngredientList().add(ing);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching recipes", e);
        }
        return new ArrayList<>(byId.values());
    }

    public Boolean deleteRecipeById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Cannot delete recipe without Id");
        }

        String sql = """
                DELETE FROM recipes WHERE id = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int rows = stmt.executeUpdate();
            return rows == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting recipe id = " + id, e);
        }
    }
}
