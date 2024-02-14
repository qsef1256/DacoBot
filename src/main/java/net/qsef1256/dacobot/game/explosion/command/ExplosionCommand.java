package net.qsef1256.dacobot.game.explosion.command;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.qsef1256.dacobot.command.DacoCommand;
import net.qsef1256.dacobot.ui.DiaMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ExplosionCommand extends DacoCommand {

    public ExplosionCommand() {
        name = "폭발";
        help = "폭발은 예술이다...";
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        event.reply(DiaMessage.underConstruction()).queue();
    }

}
