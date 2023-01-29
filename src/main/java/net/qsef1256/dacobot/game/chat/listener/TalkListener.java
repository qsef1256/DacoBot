package net.qsef1256.dacobot.game.chat.listener;

import com.jagrosh.jdautilities.command.CommandListener;
import com.jagrosh.jdautilities.command.SlashCommand;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.qsef1256.dacobot.DacoBot;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class TalkListener implements CommandListener {

    @Override
    public void onNonCommandMessage(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();
        String prefix = DacoBot.getCommandClient().getPrefix();
        if (!message.startsWith(prefix.trim())) {
            // TODO: DacoChat

            event.getChannel().sendMessage("모?루").queue();
        }
    }

    @Override
    public void onSlashCommandException(@NotNull SlashCommandEvent event, SlashCommand command, @NotNull Throwable throwable) {
        event.replyEmbeds(DiaEmbed.error(null, null, throwable, event.getUser()).build()).queue();
    }

}
