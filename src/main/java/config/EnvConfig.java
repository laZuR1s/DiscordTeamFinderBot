package config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {

    private static final Dotenv dotenv = Dotenv.load();

    public static String getDiscordToken() {
        return dotenv.get("DISCORD_API_TOKEN");
    }

    public  static String getDatabaseUrl() {
        return dotenv.get("DB_URL");
    }

    public static String getDatabaseUser() {
        return dotenv.get("DB_USER");
    }

    public static String getDatabasePassword() {
        return dotenv.get("DB_PASSWORD");
    }
}