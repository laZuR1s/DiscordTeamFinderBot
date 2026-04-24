package repository;

import config.DatabaseConfig;
import liquibase.database.Database;
import model.Application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicationRepository {

    public Application getNextApplication(long userId) {

        String sql = """
                SELECT a.application_id, a.title, a.description, a.players_needed, a.user_id, g.game_name
                FROM applications a
                JOIN game g ON a.game_id = g.game_id
                WHERE a.user_id != ?
                AND a.application_id NOT IN (
                    SELECT r.application_id
                    FROM reactions r
                    WHERE r.user_id = ?
                )
                ORDER BY a.created_at DESC
                LIMIT 1;
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            statement.setLong(2, userId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Application(
                        resultSet.getInt("application_id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getInt("players_needed"),
                        resultSet.getLong("user_id"),
                        resultSet.getString("game_name")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public long getApplicationOwner(int applicationId) {

        String sql = """
                SELECT user_id 
                FROM applications a
                WHERE application_id = ?;
                """;
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, applicationId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong("user_id");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return -1;
    }

    public boolean hasApplication(long discordId) {

        String sql = """
                SELECT 1
                FROM applications a
                JOIN users u ON a.user_id = u.user_id
                WHERE u.discord_id = ?;
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, discordId);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
