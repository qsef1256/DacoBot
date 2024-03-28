package net.qsef1256.dacobot.command;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.qsef1256.dacobot.core.ui.command.DacoCommand;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class PingCommand extends DacoCommand {

    public PingCommand() {
        name = "핑";
        help = "퐁!";
    }

    @Override
    public void runCommand(@NotNull SlashCommandEvent event) {
        long time = System.currentTimeMillis();
        event.deferReply().queue(response ->
                response.editOriginal(String.format("퐁! `%d ms`", System.currentTimeMillis() - time)).queue());
    }

}
