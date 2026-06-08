package listener;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
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

        if (modalId.startsWith("create_application_")) {


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
                            ✅ Анкета успешно создана!
                            
                            Теперь можешь искать тиммейтов или управлять своей анкетой.
                            """)
                    .addActionRow(
                            Button.primary("my_app", "📄 Моя анкета"),
                            Button.success("find", "🔍 Искать тиммейта"),
                            Button.secondary("edit_app", "✏ Изменить анкету")
                    )
                    .queue();
        }

        if (modalId.startsWith("edit_application_")) {

            int gameId = Integer.parseInt(modalId.split("_")[2]);
            long discordId = event.getUser().getIdLong();
            int userId = UserRepository.getUserIdByDiscordId(discordId);

            applicationRepository.updateApplicationFull(
                    userId,
                    gameId,
                    event.getValue("description").getAsString(),
                    Integer.parseInt(event.getValue("players").getAsString())
            );

            applicationRepository.updateStatus(userId, 1);

            event.reply("✏ Анкета обновлена")
                    .addActionRow(
                            Button.primary("my_app", "📄 Моя анкета"),
                            Button.success("find", "🔍 Искать тиммейта")
                    )
                    .queue();

        }
    }
}