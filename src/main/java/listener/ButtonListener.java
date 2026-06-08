package listener;

import Service.MatchmakingService;
import model.Application;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import repository.ApplicationRepository;
import repository.UserRepository;

public class ButtonListener extends ListenerAdapter {

    private final MatchmakingService matchmakingService;
    private final ApplicationRepository applicationRepository;

    public ButtonListener(MatchmakingService matchmakingService, ApplicationRepository applicationRepository) {
        this.matchmakingService = matchmakingService;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        String id = event.getComponentId();
        long discordId = event.getUser().getIdLong();
        long userId = UserRepository.getUserIdByDiscordId(discordId);

        switch (id) {
            case "create_app" -> {

                SelectMenu menu = StringSelectMenu.create("select_game_create")
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

                Application app = applicationRepository.getUserApplication(userId);

                if (app == null) {
                    event.reply("""
                            У тебя пока нет анкеты 😢
                            
                            Создай её через кнопку
                            "Создать анкету"
                            """).addActionRow(
                            Button.primary("create_app", "Создать анкету")
                    ).queue();
                    return;
                }

                String status = app.getStatusId() == 1 ? "Активна" : "Неактивна";

                event.reply("""
                                📄 Твоя анкета
                                
                                🎮 Игра: %s
                                
                                📝 Описание:
                                %s
                                
                                👥 Нужно игроков: %d
                                
                                📌 Статус: %s
                                """.formatted(
                                app.getGame(),
                                app.getDescription(),
                                app.getPlayersNeeded(),
                                status
                        ))
                        .addActionRow(
                                Button.secondary(
                                        "back_menu",
                                        "◀ Назад"
                                ),
                                Button.primary(
                                        "edit_app",
                                        "✏ Изменить"
                                ),
                                app.getStatusId() == 1
                                        ? Button.danger(
                                        "stop_search",
                                        "⏸ Прекратить искать"
                                )
                                        : Button.success(
                                        "resume_search",
                                        "▶ Возобновить поиск"
                                )
                        )
                        .queue();

                return;
            }
            case "edit_app" -> {

                Application app =
                        applicationRepository.getUserApplication(userId);

                if (app == null) {
                    event.reply("❌ У тебя нет анкеты")
                            .queue();
                    return;
                }

                SelectMenu menu = StringSelectMenu.create("select_game_edit")
                        .setPlaceholder("🎮 Выбери новую игру")
                        .addOption("CS2", "1")
                        .addOption("Dota 2", "2")
                        .addOption("Valorant", "3")
                        .addOption("Minecraft", "4")
                        .build();

                event.reply("✏ Выбери игру для изменения анкеты")
                        .addActionRow(menu)
                        .queue();

                return;
            }
            case "find" -> {

                Application userApp =
                        applicationRepository.getUserApplication(userId);

                if (userApp == null) {

                    event.reply("""
                                    ❌ У тебя нет анкеты
                                    
                                    Создай анкету, чтобы начать поиск тиммейтов
                                    """)
                            .addActionRow(
                                    Button.primary(
                                            "create_app",
                                            "➕ Создать анкету"
                                    )
                            )
                            .queue();

                    return;
                }

                if (userApp.getStatusId() != 1) {

                    event.reply("""
                                    ⛔ Поиск отключён
                                    
                                    Твоя анкета сейчас неактивна.
                                    Включи поиск в разделе "Моя анкета"
                                    """)
                            .addActionRow(
                                    Button.secondary(
                                            "back_menu",
                                            "◀ Назад"
                                    ),
                                    Button.success(
                                            "resume_search",
                                            "▶ Включить поиск"
                                    )
                            )
                            .queue();

                    return;
                }

                Application app = matchmakingService.getNext(userId);

                if (app == null) {
                    event.reply("😢 Сейчас нет доступных анкет")
                            .addActionRow(
                                    Button.secondary("back_menu", "◀ Назад")
                            )
                            .queue();
                    return;
                }

                event.reply("""
                                Анкета
                                
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

            case "back_menu" -> {

                event.reply("🏠 Главное меню")
                        .addActionRow(
                                Button.primary(
                                        "my_app",
                                        "📄 Моя анкета"
                                ),

                                Button.success(
                                        "find",
                                        "🔍 Искать тиммейта"
                                )
                        )
                        .queue();

                return;
            }


            case "stop_search" -> {
                applicationRepository.updateStatus(userId, 0);

                event.reply("""
                                ⏸ Поиск приостановлен
                                
                                Твоя анкета теперь не будет показываться другим пользователям
                                """)
                        .addActionRow(
                                Button.primary(
                                        "my_app",
                                        "📄 Моя анкета"
                                ),
                                Button.success(
                                        "resume_search",
                                        "▶ Возобновить поиск"
                                )
                        )
                        .queue();
            }

            case "resume_search" -> {
                applicationRepository.updateStatus(userId, 1);

                event.reply("""
                                ▶ Поиск возобновлен
                                
                                Твоя анкета снова будет показываться другим пользователям
                                """)
                        .addActionRow(
                                Button.primary(
                                        "my_app",
                                        "📄 Моя анкета"
                                ),
                                Button.danger(
                                        "stop_search",
                                        "⏸ Прекратить искать"
                                )
                        )
                        .queue();
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


    private void sendNext(ButtonInteractionEvent event) {

        long discordId = event.getUser().getIdLong();
        long userId = UserRepository.getUserIdByDiscordId(discordId);

        Application application = matchmakingService.getNext(userId);

        if (application == null) {
            event.reply("😢 Сейчас нет доступных анкет")
                    .addActionRow(
                            Button.secondary("back_menu", "◀ Назад")
                    )
                    .queue();
            return;
        }

        event.reply("""
                        Анкета:
                        
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
