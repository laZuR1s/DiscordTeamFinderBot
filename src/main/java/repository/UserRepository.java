package repository;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public static String getSteamId(long userId) {

        String sql = """
            SELECT steam_id
            FROM users
            WHERE user_id = ?
            """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getString("steam_id");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static void updateSteamIdByDiscordId(long discordId, String steamId) {

        String sql = """
                UPDATE users
                SET steam_id = ?
                WHERE discord_id = ?
                """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, Long.parseLong(steamId));
            ps.setLong(2, discordId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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

    public static int getUserIdByDiscordId(long discordId) {

        String sql = "SELECT user_id FROM users WHERE discord_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, discordId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }

            throw new RuntimeException("User not found");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
