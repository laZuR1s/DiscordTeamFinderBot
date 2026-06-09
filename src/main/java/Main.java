import bot.DiscordBot;
import config.DatabaseMigration;
import controller.SteamAuthController;
import io.javalin.Javalin;

public class Main {

    public static void main(String[] args) {

        DatabaseMigration.migrate();

        new DiscordBot().start();
        Javalin app = Javalin.create().start(8080);

        SteamAuthController.register(app);
    }
}
