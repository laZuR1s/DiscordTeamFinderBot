package command;

import listener.SlashCommandListener;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import repository.ApplicationRepository;
import repository.UserRepository;

public class StartCommand {

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public StartCommand(UserRepository userRepository, ApplicationRepository applicationRepository) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }

    public void execute(SlashCommandInteractionEvent event) {

        long discordId = event.getUser().getIdLong();
        String username = event.getUser().getName();


        if (!userRepository.existsByDiscordId(discordId)) {
            userRepository.save(discordId, username);
        }

        boolean hasApplications = applicationRepository.hasApplication(discordId);

        if(!hasApplications) {
            event.reply("👋 Добро пожаловать!\nУ тебя пока нет анкеты.")
                    .addActionRow(
                            Button.primary("create_app","➕ Создать анкету"),
                            Button.secondary("profile", "👤 Профиль")
                    )
                    .queue();

            return;
        }

        event.reply("🏠 Главное меню")
                .addActionRow(
                        Button.primary("my_app","📄 Моя анкета" ),
                        Button.success("find","🔍 Искать тиммейта"),
                        Button.secondary("edit_app", "✏ Изменить анкету")
                )
                .queue();
    }
}