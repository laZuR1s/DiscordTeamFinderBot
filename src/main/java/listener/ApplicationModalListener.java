package listener;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import repository.ApplicationRepository;
import repository.GameRepository;
import repository.UserRepository;

public class ApplicationModalListener extends ListenerAdapter {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    public ApplicationModalListener(ApplicationRepository applicationRepository, UserRepository userRepository, GameRepository gameRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {

        String modalId = event.getModalId();

        if (!modalId.startsWith("create_application_")) {
            return;
        }

        long discordId = event.getUser().getIdLong();
        int userId = UserRepository.getUserIdByDiscordId(discordId);
        int gameId = Integer.parseInt(
                modalId.split("_")[2]
        );


        String description = event.getValue("description").getAsString();

        int playersNeeded = Integer.parseInt(
                event.getValue("players").getAsString()
        );

        applicationRepository.createApplication(
                userId,
                gameId,
                description,
                playersNeeded
        );

        event.reply("""
                ✅ Анкета создана!
                
                🎮 Game : %s
                📝 %s
                👥 Нужно: %d
                """.formatted(
                gameRepository.getGameName(gameId),
                description,
                playersNeeded
        )).setEphemeral(true).queue();
    }
}