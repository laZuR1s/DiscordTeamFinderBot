package listener;

import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class SelectMenuListener extends ListenerAdapter {

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {

        String id = event.getComponentId();

        if (id.equals("select_game_create")) {
            handleCreate(event);
            return;
        }

        if (id.equals("select_game_edit")) {
            handleEdit(event);
            return;
        }
    }

    private void handleCreate(StringSelectInteractionEvent event) {

        String gameId = event.getValues().get(0);

        Modal modal = Modal.create(
                        "create_application_" + gameId,
                        "Создание анкеты"
                )
                .addActionRow(
                        TextInput.create(
                                "description",
                                "Описание",
                                TextInputStyle.PARAGRAPH
                        ).setRequired(true).build()
                )
                .addActionRow(
                        TextInput.create(
                                "players",
                                "Игроки",
                                TextInputStyle.SHORT
                        ).setRequired(true).build()
                )
                .build();

        event.replyModal(modal).queue();
    }

    private void handleEdit(StringSelectInteractionEvent event) {

        String gameId = event.getValues().get(0);

        Modal modal = Modal.create(
                        "edit_application_" + gameId,
                        "Редактирование анкеты"
                )
                .addActionRow(
                        TextInput.create(
                                "description",
                                "Описание",
                                TextInputStyle.PARAGRAPH
                        ).setRequired(true).build()
                )
                .addActionRow(
                        TextInput.create(
                                "players",
                                "Игроки",
                                TextInputStyle.SHORT
                        ).setRequired(true).build()
                )
                .build();

        event.replyModal(modal).queue();
    }
}
