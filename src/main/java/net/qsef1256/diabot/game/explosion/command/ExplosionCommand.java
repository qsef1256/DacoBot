package net.qsef1256.diabot.game.explosion.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class ExplosionCommand extends SlashCommand {

    public ExplosionCommand() {
        name = "폭발";
        help = "폭발은 예술이다...";

        //children = new SlashCommand[]{};
    }

    @Override
    protected void execute(final SlashCommandEvent event) {
        event.reply("준비 중! : " + getHelp()).queue();
    }

}
