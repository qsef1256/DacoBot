package net.qsef1256.diabot.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class PingCommand extends SlashCommand {
    public PingCommand() {
        this.name = "핑";
        this.help = "퐁!";
    }

    @Override
    public void execute(SlashCommandEvent event) {
        long time = System.currentTimeMillis();
        event.deferReply()
                .queue(response -> response.editOriginal(String.format("퐁! `%d ms`", System.currentTimeMillis() - time)).queue());
    }
}
