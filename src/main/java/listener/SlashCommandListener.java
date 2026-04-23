package listener;

import command.FindTeammateCommand;
import command.RegisterCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter {

    private final RegisterCommand registerCommand;
    private final FindTeammateCommand findTeammateCommand;


    public SlashCommandListener(RegisterCommand registerCommand, FindTeammateCommand findTeammateCommand) {
        this.registerCommand = registerCommand;
        this.findTeammateCommand = findTeammateCommand;
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
        }
    }
}
