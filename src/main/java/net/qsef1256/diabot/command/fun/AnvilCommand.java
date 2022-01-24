package net.qsef1256.diabot.command.fun;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.qsef1256.diabot.enums.DiaImage;
import net.qsef1256.diabot.model.CmdStatistic;
import net.qsef1256.diabot.util.CommonUtil;

import java.awt.*;

public class AnvilCommand extends SlashCommand {

    public AnvilCommand() {
        name = "모루";
        help = "몰?루";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        if (event.getMember() == null) return;
        User user = event.getUser();
        CmdStatistic statistic = new CmdStatistic(getClass());

        int random = CommonUtil.randomInt(1, 2);
        switch (random) {
            case 1 -> event.replyEmbeds(new EmbedBuilder()
                    .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                    .setColor(new Color(35, 35, 35))
                    .setTitle("모?루")
                    .setImage(DiaImage.ANVIL)
                    .setFooter(statistic.getUseInfo())
                    .build()).queue();
            case 2 -> event.replyEmbeds(new EmbedBuilder()
                    .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                    .setColor(new Color(27, 221, 239))
                    .setTitle("Mola mola")
                    .setImage(DiaImage.MOLA_MOLA)
                    .setFooter(statistic.getUseInfo())
                    .build()).queue();
        }

    }
}
