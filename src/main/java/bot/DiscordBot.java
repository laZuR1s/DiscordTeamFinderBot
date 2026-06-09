package bot;

import Service.MatchmakingService;
import command.FindTeammateCommand;
import command.RegisterCommand;
import command.StartCommand;
import config.EnvConfig;
import listener.ApplicationModalListener;
import listener.ButtonListener;
import listener.SelectMenuListener;
import listener.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import repository.ApplicationRepository;
import repository.GameRepository;
import repository.ReactionRepository;
import repository.UserRepository;

public class DiscordBot {

    String token = EnvConfig.getDiscordToken();

    public void start() {


        UserRepository userRepository = new UserRepository();
        ApplicationRepository applicationRepository = new ApplicationRepository();
        ReactionRepository reactionRepository = new ReactionRepository();
        GameRepository gameRepository = new GameRepository();

        MatchmakingService matchmakingService = new MatchmakingService(applicationRepository, reactionRepository);

        RegisterCommand registerCommand = new RegisterCommand(userRepository);
        FindTeammateCommand findTeammateCommand = new FindTeammateCommand(matchmakingService);
        StartCommand startCommand = new StartCommand(userRepository, applicationRepository);

        JDA jda = JDABuilder.createDefault(token,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(
                        new SlashCommandListener(registerCommand, findTeammateCommand, startCommand),
                        new ButtonListener(matchmakingService, applicationRepository),
                        new ApplicationModalListener(applicationRepository, userRepository, gameRepository),
                        new SelectMenuListener())
                .build();

        jda.updateCommands().addCommands(
                Commands.slash("register", "Register yourself"),
                Commands.slash("findteammate", "Find teammates"),
                Commands.slash("start", "Start the bot")
        ).queue();
    }
}
