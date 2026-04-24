package command;

import model.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import repository.UserRepository;

public class RegisterCommand {

    private final UserRepository userRepository;

    public RegisterCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(SlashCommandInteractionEvent event) {

        long discordId = event.getUser().getIdLong();
        String username = event.getUser().getName();


        if (userRepository.existsByDiscordId(discordId)) {
            event.reply("Ты уже зарегистрирован!").queue();
            return;
        }

        userRepository.save(discordId, username);

        event.reply("Ты зарегистрирован 👍").queue();

    }
}
