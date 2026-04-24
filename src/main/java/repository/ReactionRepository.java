package repository;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReactionRepository {


    public void saveReaction(long userId, int applicationId, boolean isLike) {

        String sql = """
                INSERT INTO reactions (user_id, application_id, is_like)
                VALUES (?, ?, ?)
                ON CONFLICT (user_id, application_id)
                DO UPDATE SET is_like= EXCLUDED.is_like;
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            statement.setInt(2, applicationId);
            statement.setBoolean(3, isLike);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasUserLikedMe(long currentUserid, long targetUserId) {

        String sql = """
                SELECT 1
                FROM reactions r
                JOIN applications a ON r.application_id = a.application_id
                WHERE r.user_id = ?
                AND a.user_id = ?
                AND r.is_like = true
                LIMIT 1;
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, targetUserId);
            statement.setLong(2, currentUserid);

            ResultSet rs = statement.executeQuery();


            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean alreadyReacted(long userId, long applicationId) {

        String sql = """
                SELECT 1
                From reactions r
                WHERE user_id = ? AND r.application_id = ?
                """;

        try (Connection connection= DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            statement.setLong(2, applicationId);

            ResultSet rs = statement.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
