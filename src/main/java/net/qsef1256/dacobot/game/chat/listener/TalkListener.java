package net.qsef1256.dacobot.game.chat.listener;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandListener;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TalkListener implements CommandListener {

    @Setter(onMethod_ = {@Autowired})
    private CommandClient commandClient;

    @Override
    public void onNonCommandMessage(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();
        String prefix = commandClient.getPrefix();
        if (message.startsWith(prefix.trim())) {
            // TODO: DacoChat

            event.getChannel().sendMessage("모?루").queue();
        }
    }

    @Override
    public void onSlashCommandException(@NotNull SlashCommandEvent event,
                                        @NotNull SlashCommand command,
                                        @NotNull Throwable throwable) {
        event.replyEmbeds(DiaEmbed.error(null, null, throwable, event.getUser()).build()).queue();
    }

}
