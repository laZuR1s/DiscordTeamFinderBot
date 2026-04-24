package command;

import Service.MatchmakingService;
import model.Application;
import model.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import repository.UserRepository;

import java.util.List;

public class FindTeammateCommand {

    private final MatchmakingService matchmakingService;

    public FindTeammateCommand(MatchmakingService matchmakingService) {
        this.matchmakingService = matchmakingService;
    }

    public void execute(SlashCommandInteractionEvent event) {

        long discordId = event.getUser().getIdLong();
        long userId = UserRepository.getUserIdByDiscordId(discordId);

        Application application = matchmakingService.getNext(userId);

        if (application == null) {
            event.reply("Анкеты закончились").queue();
            return;
        }

        event.reply("""
                        🎮 **%s**
                        📝 %s
                        👥 Нужно игроков: %d
                        """.formatted(
                        application.getGame(),
                        application.getDescription(),
                        application.getPlayersNeeded()
                ))
                .addActionRow(
                        Button.success("like_" + application.getId(), "👍"),
                        Button.danger("dislike_" + application.getId(), "👎")
                )
                .queue();
    }
}


