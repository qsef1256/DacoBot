package net.qsef1256.dacobot.game.explosion.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.qsef1256.dacobot.ui.DiaMessage;
import org.jetbrains.annotations.NotNull;

public class ShopCommand extends SlashCommand {

    public ShopCommand() {
        name = "상점";
        help = "테미 샵 비슷한 거...";
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        event.reply(DiaMessage.underConstruction()).queue();
    }

}
