package listener;

import command.FindTeammateCommand;
import command.RegisterCommand;
import command.StartCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter {

    private final RegisterCommand registerCommand;
    private final FindTeammateCommand findTeammateCommand;
    private final StartCommand startCommand;

    public SlashCommandListener(RegisterCommand registerCommand, FindTeammateCommand findTeammateCommand, StartCommand startCommand) {
        this.registerCommand = registerCommand;
        this.findTeammateCommand = findTeammateCommand;
        this.startCommand = startCommand;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        switch (event.getName()) {

            case "register":
                registerCommand.execute(event);
                break;
            case "findteammate":
                findTeammateCommand.execute(event);
                break;
            case "start":
                startCommand.execute(event);
                break;
        }
    }
}
