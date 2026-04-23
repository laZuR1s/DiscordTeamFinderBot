package command;

import Service.MatchmakingService;
import model.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;

public class FindTeammateCommand {

    private final MatchmakingService matchmakingService;

    public FindTeammateCommand(MatchmakingService matchmakingService) {
        this.matchmakingService = matchmakingService;
    }

    public void execute(SlashCommandInteractionEvent event) {

        String game = "cs2";
        List<User> users = matchmakingService.findTeammates(game, event.getUser().getId());

        if (users.isEmpty()) {
            event.reply("К сожалению, сейчас нет игроков для " + game).queue();
            return;
        }


        StringBuilder response = new StringBuilder("Вот кого я нашел:\n");

        for (User user : users) {
            response.append(user.getUsername()).append(" (").append(user.getRank()).append(")\n");
        }

        event.reply(response.toString())
                .setEphemeral(true)
                .queue();
    }
}

