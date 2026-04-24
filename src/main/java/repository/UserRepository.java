package repository;

import config.DatabaseConfig;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepository {

    private static final List<User> users = new ArrayList<>();

    public void save(long discordId, String username) {

        String sql = """
                INSERT INTO users (discord_id, username) VALUES (?, ?)
                ON CONFLICT (discord_id) DO NOTHING""";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, discordId);
            stmt.setString(2, username);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsByDiscordId(long discordId) {

        String sql = """
                SELECT 1
                FROM users
                WHERE discord_id = ?""";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, discordId);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static long getUserIdByDiscordId(long discordId) {

        String sql = "SELECT user_id FROM users WHERE discord_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, discordId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getLong("user_id");
            }

            throw new RuntimeException("User not found");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
