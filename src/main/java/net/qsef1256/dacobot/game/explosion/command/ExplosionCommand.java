package net.qsef1256.dacobot.game.explosion.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.qsef1256.dacobot.ui.DiaMessage;
import org.jetbrains.annotations.NotNull;

public class ExplosionCommand extends SlashCommand {

    public ExplosionCommand() {
        name = "폭발";
        help = "폭발은 예술이다...";
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        event.reply(DiaMessage.underConstruction()).queue();
    }

}
