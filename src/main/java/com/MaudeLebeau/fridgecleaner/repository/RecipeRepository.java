package com.MaudeLebeau.fridgecleaner.repository;

import com.MaudeLebeau.fridgecleaner.domain.Ingredient;
import com.MaudeLebeau.fridgecleaner.domain.Recipe;
import com.MaudeLebeau.fridgecleaner.domain.Unit;

import java.sql.*;
import java.util.ArrayList;

public class RecipeRepository {
    public Recipe addRecipe(Recipe recipe) {
        String recipeSql = """
                INSERT INTO recipes (name, servings)
                VALUES (?, ?)
                """;

        String ingredientSql = """
                INSERT INTO recipe_ingredients(recipe_id, item_id, quantity, unit)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);

            try {
                long recipeId;

                try (PreparedStatement stmt = conn.prepareStatement(recipeSql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, recipe.getName());
                    stmt.setInt(2, recipe.getServing());
                    stmt.executeUpdate();

                    try (ResultSet keys = stmt.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Faile to retrieve recipe generated ID");
                        }
                        recipeId = keys.getLong(1);
                    }
                }

                if (recipe.getIngredientList() != null &&  !recipe.getIngredientList().isEmpty()) {
                    try (PreparedStatement stmt = conn.prepareStatement(ingredientSql)) {
                        for (Ingredient ing : recipe.getIngredientList()) {
                            stmt.setLong(1, recipeId);
                            stmt.setLong(2, ing.getId());
                            stmt.setBigDecimal(3, ing.getQuantity());
                            stmt.setString(4, ing.getUnit().toString());
                            stmt.addBatch();
                        }
                        stmt.executeBatch();
                    }
                }

                conn.commit();

                return new Recipe(recipeId, recipe.getName(), recipe.getServing(), recipe.getIngredientList());

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting reciep", e);
        }
    }

    public Recipe getRecipeById(Long id) {
        String recipeSql = """
                SELECT id, name, servings FROM recipes WHERE id = ?
                """;

        String ingredientsSql = """
                SELECT i.id AS item_id,
                i.name AS item_name,
                ri.quantity, ri.unit
                FROM recipe_ingredients ri
                JOIN items i ON i.id = ri.item_id
                WHERE ri.recipe_id = ?
                ORDER BY i.name
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
                            new ArrayList<>()
                    );
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(ingredientsSql)) {
                stmt.setLong(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Ingredient ing = new Ingredient(
                                rs.getLong("item_id"),
                                rs.getString("item_name"),
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
}
