package util;

import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class MenuUtil {

    public static void sendMainMenu(IReplyCallback event) {

        event.reply("🏠 Главное меню")
                .addActionRow(
                        Button.primary("my_app", "📄 Моя анкета"),
                        Button.success("find", "🔍 Искать тиммейта")
                )
                .queue();
    }

    public static void sendMainMenu(
            IReplyCallback event,
            String message
    ) {
        event.reply(message+ """
                        
                        🏠 Главное меню
                        """)
                .addActionRow(
                        Button.primary("my_app", "📄 Моя анкета"),
                        Button.success("find", "🔍 Искать тиммейта")
                )
                .queue();
    }
}