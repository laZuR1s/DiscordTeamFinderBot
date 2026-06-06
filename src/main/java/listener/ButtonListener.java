package listener;

import Service.MatchmakingService;
import model.Application;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import repository.UserRepository;

public class ButtonListener extends ListenerAdapter {

    private final MatchmakingService matchmakingService;

    public ButtonListener(MatchmakingService matchmakingService) {
        this.matchmakingService = matchmakingService;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        String id = event.getComponentId();
        long discordId = event.getUser().getIdLong();
        long userId = UserRepository.getUserIdByDiscordId(discordId);

        switch (id) {
            case "create_app" -> {

                StringSelectMenu menu = StringSelectMenu.create("select_game")
                        .setPlaceholder("Выбери игру")
                        .addOption("CS2", "1")
                        .addOption("Dota 2", "2")
                        .addOption("Valorant", "3")
                        .addOption("League of Legends", "4")
                        .build();

                event.reply("🎮 Выбери игру для анкеты:")
                        .addActionRow(menu)
                        .queue();

                return;
            }
            case "my_app" -> {
                event.reply("📄 Твоя анкета (пока пусто)")
                        .queue();
                return;
            }
            case "edit_app" -> {
                event.reply("✏ Редактирование анкеты (пока пусто)")
                        .queue();
                return;
            }
            case "find" -> {

                Application app = matchmakingService.getNext(userId);

                if (app == null) {
                    event.reply("😢 Сейчас нет доступных анкет")
                            .queue();
                    return;
                }

                event.reply("""
                                🎮 %s
                                📝 %s
                                👥 Нужно: %d
                                """.formatted(
                                app.getGame(),
                                app.getDescription(),
                                app.getPlayersNeeded()
                        ))
                        .addActionRow(
                                Button.success("like_" + app.getId(), "👍"),
                                Button.danger("dislike_" + app.getId(), "👎")
                        )
                        .queue();

                return;
            }
        }

        if (id.startsWith("like_")) {
            int applicationId = Integer.parseInt(id.split("_")[1]);

            boolean isMatch = matchmakingService.like(userId, applicationId);

            if (isMatch) {
                event.reply("🔥 MATCH! У вас совпаденние!")
                        .queue();
            } else {
                sendNext(event);
            }
        }

        if (id.startsWith("dislike_")) {

            int applicationId = Integer.parseInt(id.split("_")[1]);
            matchmakingService.dislike(userId, applicationId);
            sendNext(event);
        }


    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {

        if (!event.getComponentId().equals("select_game")) return;

        String gameId = event.getValues().get(0);

        Modal modal =
                Modal.create(
                                "create_application_" + gameId,
                                "Создание анкеты"
                        )
                        .addActionRow(
                                TextInput.create(
                                                "description",
                                                "Описание",
                                                TextInputStyle.PARAGRAPH
                                        )
                                        .setRequired(true)
                                        .build()
                        )
                        .addActionRow(
                                TextInput.create(
                                                "players",
                                                "Сколько игроков нужно",
                                                TextInputStyle.SHORT
                                        )
                                        .setRequired(true)
                                        .build()
                        )
                        .build();

        event.replyModal(modal).queue();
    }


    private void sendNext(ButtonInteractionEvent event) {

        long discordId = event.getUser().getIdLong();
        long userId = UserRepository.getUserIdByDiscordId(discordId);

        Application application = matchmakingService.getNext(userId);

        if (application == null) {
            event.reply("😢 Сейчас нет доступных анкет").queue();
            return;
        }

        event.reply("""
                        🎮 %s
                        📝 %s
                        👥 Нужно: %d
                        """.formatted(application.getGame(), application.getDescription(), application.getPlayersNeeded()))
                .addActionRow(
                        Button.success("like_" + application.getId(), "👍"),
                        Button.danger("dislike_" + application.getId(), "👎")
                )
                .queue();
    }

}
