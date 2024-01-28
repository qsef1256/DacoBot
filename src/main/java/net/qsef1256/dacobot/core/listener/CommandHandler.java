package net.qsef1256.dacobot.core.listener;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandListener;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommandHandler implements CommandListener {

    private final CommandClient commandClient;

    @Autowired
    public CommandHandler(CommandClient commandClient) {
        this.commandClient = commandClient;
    }

    @Override
    public void onSlashCommandException(@NotNull SlashCommandEvent event,
                                        @NotNull SlashCommand command,
                                        @NotNull Throwable throwable) {
        log.error("%s slash command exception".formatted(command.getName()), throwable);

        event.replyEmbeds(DiaEmbed.error(null, null, throwable, event.getUser()).build()).queue();
    }

    @Override
    public void onNonCommandMessage(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();
        String prefix = commandClient.getPrefix();
        if (message.startsWith(prefix.trim())) {
            // TODO: DacoChat

            event.getChannel().sendMessage("모?루").queue();
        }
    }

}
