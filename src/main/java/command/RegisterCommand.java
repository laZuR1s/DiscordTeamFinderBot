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
        String game= "cs2";
        String rank = "gold";

        User user = new User(
                event.getUser().getId(),
                event.getUser().getName(),
                game,
                rank
        );

        UserRepository.save(user);

        event.reply("Ты зарегистрирован 👍").queue();
    }
}
