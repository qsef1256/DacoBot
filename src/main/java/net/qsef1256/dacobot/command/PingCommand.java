package net.qsef1256.dacobot.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;

public class PingCommand extends SlashCommand {

    public PingCommand() {
        name = "핑";
        help = "퐁!";
    }

    @Override
    public void execute(@NotNull SlashCommandEvent event) {
        long time = System.currentTimeMillis();
        event.deferReply().queue(response ->
                response.editOriginal(String.format("퐁! `%d ms`", System.currentTimeMillis() - time)).queue());
    }

}
