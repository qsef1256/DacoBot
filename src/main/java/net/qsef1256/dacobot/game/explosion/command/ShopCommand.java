package net.qsef1256.dacobot.game.explosion.command;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.qsef1256.dacobot.core.command.DacoCommand;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ShopCommand extends DacoCommand {

    public ShopCommand() {
        name = "상점";
        help = "테미 샵 비슷한 거...";
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        event.reply(underConstruction()).queue();
    }

}
