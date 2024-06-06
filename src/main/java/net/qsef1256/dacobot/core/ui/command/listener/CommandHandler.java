package net.qsef1256.dacobot.core.ui.command.listener;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandListener;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.qsef1256.dacobot.core.ui.command.exception.CommandExceptionHandler;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class CommandHandler implements CommandListener {

    private final CommandClient commandClient;
    private final List<CommandExceptionHandler<?>> handlers;

    @Override
    public void onSlashCommandException(@NotNull SlashCommandEvent event,
                                        @NotNull SlashCommand command,
                                        @NotNull Throwable throwable) {
        CommandExceptionHandler<?> handler = handlers.stream()
                .filter(exceptionHandler -> exceptionHandler.getException().equals(throwable.getClass()))
                .findFirst()
                .orElse(null);
        if (handler != null) {
            handleException(handler, event, throwable);

            return;
        }

        log.error("%s slash command exception".formatted(command.getName()), throwable);
        event.replyEmbeds(DiaEmbed.error(null,
                null,
                throwable,
                event.getUser()).build()).queue();
    }

    @SuppressWarnings("unchecked")
    private <E extends Exception> void handleException(
            @NotNull CommandExceptionHandler<?> handler,
            @NotNull SlashCommandEvent event,
            @NotNull Throwable throwable) {
        ((CommandExceptionHandler<E>) handler).onException(event, (E) throwable);
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
