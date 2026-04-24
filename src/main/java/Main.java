import bot.DiscordBot;
import config.DatabaseMigration;

public class Main {

    public static void main(String[] args) {

        DatabaseMigration.migrate();

        new DiscordBot().start();

    }
}
