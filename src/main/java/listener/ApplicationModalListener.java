package listener;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import repository.ApplicationRepository;
import repository.GameRepository;
import repository.UserRepository;
import util.MenuUtil;

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

            if (description.length() > 500) {

                MenuUtil.sendMainMenu(event, """
                        ❌ Максимум 500 символов
                        """);

                return;
            }

            String playersNeeded =
                    event.getValue("players").getAsString();

            int players;

            try {
                players = Integer.parseInt(playersNeeded);
            } catch (NumberFormatException e) {

                MenuUtil.sendMainMenu(event, """
                        ❌ Количество игроков должно быть числом
                        """);

                return;
            }

            if (players < 1 || players > 10) {

                MenuUtil.sendMainMenu(event, """
                        ❌ Количество игроков должно быть от 1 до 10
                        """);

                return;
            }

            applicationRepository.createApplication(
                    userId,
                    gameId,
                    description,
                    players
            );

            MenuUtil.sendMainMenu(event, """
                    ✅ Анкета создана
                    """);

            MenuUtil.sendMainMenu(event);
        }

        if (modalId.startsWith("edit_application_")) {

            int gameId = Integer.parseInt(modalId.split("_")[2]);
            long discordId = event.getUser().getIdLong();
            int userId = UserRepository.getUserIdByDiscordId(discordId);


            String description = event.getValue("description").getAsString();

            if (description.length() > 500) {

                MenuUtil.sendMainMenu(event, """
                        ❌ Максимум 500 символов
                        """);

                return;
            }

            String playersNeeded =
                    event.getValue("players").getAsString();

            int players;

            try {
                players = Integer.parseInt(playersNeeded);
            } catch (NumberFormatException e) {

                MenuUtil.sendMainMenu(event, """
                        ❌ Количество игроков должно быть числом
                        """);

                return;
            }

            if (players < 1 || players > 10) {

                MenuUtil.sendMainMenu(event, """
                        ❌ Количество игроков должно быть от 1 до 10
                        """);

                return;
            }

            applicationRepository.updateApplicationFull(
                    userId,
                    gameId,
                    description,
                    players
            );

            applicationRepository.updateStatus(userId, 1);

            MenuUtil.sendMainMenu(event,
                    """
                            ✅ Анкета обновлена
                            """);

        }
    }
}