package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                EnvConfig.getDatabaseUrl(),
                EnvConfig.getDatabaseUser(),
                EnvConfig.getDatabasePassword()
        );
    }
}
