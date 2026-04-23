package bot;

import Service.MatchmakingService;
import command.FindTeammateCommand;
import command.RegisterCommand;
import listener.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import repository.UserRepository;

public class DiscordBot {

    final String DISCORD_API_TOKEN = "MTQ5MzQ5Mzk0OTM3MTc3NzAzNA.GOIi7-.Q6H4xP5Kevv9TCzdPVb78eH_r65OKqrVnQ02y8";

    public void start(){


        UserRepository userRepository = new UserRepository();
        MatchmakingService matchmakingService = new MatchmakingService(userRepository);

        RegisterCommand registerCommand = new RegisterCommand(userRepository);
        FindTeammateCommand findTeammateCommand = new FindTeammateCommand(matchmakingService);

        JDA jda = JDABuilder.createDefault(DISCORD_API_TOKEN,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new SlashCommandListener(registerCommand, findTeammateCommand))
                .build();

        jda.updateCommands().addCommands(
                Commands.slash("register", "Register yourself"),
                Commands.slash("findteammate", "Find teammates")
        ).queue();
    }
}
