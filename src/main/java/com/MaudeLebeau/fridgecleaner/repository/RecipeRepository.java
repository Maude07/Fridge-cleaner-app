package com.MaudeLebeau.fridgecleaner.repository;

import com.MaudeLebeau.fridgecleaner.domain.Ingredient;
import com.MaudeLebeau.fridgecleaner.domain.Recipe;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RecipeRepository {
    public Recipe addRecipe(Recipe recipe, List<Ingredient> ingredientsList) {
        String sql = """
                INSERT INTO recipes (name, servings)
                VALUES (?, ?)
                """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt =
                conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

             stmt.setString(1, recipe.getName());

             stmt.setInt(2, recipe.getServing());

             stmt.executeUpdate();

             try (ResultSet keys = stmt.getGeneratedKeys()) {

                 if (keys.next()) {
                     Long generatedId = keys.getLong(1);

                     return new Recipe(
                             generatedId,
                             recipe.getName(),
                             recipe.getServing(),
                             ingredientsList
                     );
                 } else {
                     throw new SQLException("Failed to retrieve recipe generated ID");
                 }
             }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting recipe", e);
        }
    }

    public Recipe getRecipeById(Long id) {
        String sql = """
                SELECT id, name, servings FROM recipes WHERE id = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return new Recipe(
                        rs.getLong("id"),
                        rs.getString("name"),

                )
            }
        }
    }
}
