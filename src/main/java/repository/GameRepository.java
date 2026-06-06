package repository;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GameRepository {

    public String getGameName(long gameId) {

        String sql = """
                SELECT game_name
                FROM game
                WHERE game_id = ?
                """;

        try(Connection connection =
                    DatabaseConfig.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql)) {

            statement.setLong(1, gameId);

            ResultSet rs =
                    statement.executeQuery();

            if(rs.next()) {
                return rs.getString("game_name");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
