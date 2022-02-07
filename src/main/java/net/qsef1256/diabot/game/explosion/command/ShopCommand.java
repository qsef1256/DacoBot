package net.qsef1256.diabot.game.explosion.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class ShopCommand extends SlashCommand {

    public ShopCommand() {
        name = "상점";
        help = "테미 샵 비슷한 거...";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        event.reply(":construction: 공사중...").queue();


    }

}
