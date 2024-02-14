package net.qsef1256.dacobot.command.fun;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.command.DacoCommand;
import net.qsef1256.dacobot.setting.constants.DiaImage;
import net.qsef1256.dialib.util.RandomUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class AnvilCommand extends DacoCommand {

    public AnvilCommand() {
        name = "모루";
        help = "몰?루";

        statistic = true;
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        if (event.getMember() == null) return;
        User user = event.getUser();

        int random = RandomUtil.randomInt(1, 4);
        switch (random) {
            case 1 -> event.replyEmbeds(new EmbedBuilder()
                    .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                    .setColor(new Color(35, 35, 35))
                    .setTitle("모?루")
                    .setImage(DiaImage.ANVIL)
                    .setFooter(getUseInfo())
                    .build()).queue();
            case 2 -> event.replyEmbeds(new EmbedBuilder()
                    .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                    .setColor(new Color(27, 221, 239))
                    .setTitle("Mola mola")
                    .setImage(DiaImage.MOLA_MOLA)
                    .setFooter(getUseInfo())
                    .build()).queue();
            case 3 -> event.replyEmbeds(new EmbedBuilder()
                    .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                    .setColor(new Color(27, 126, 239))
                    .setTitle("ㅁ ㄹ")
                    .setDescription("""
                            ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛
                            ⬛🟥⬛🟥🟥⬛⬛🟥⬛🟥⬛
                            ⬛🟥⬛🟥⬛🟥⬛🟥🟥⬛⬛
                            ⬛🟥⬛🟥⬛🟥⬛🟥🟥⬛⬛
                            ⬛🟥⬛🟥🟥⬛⬛🟥⬛🟥⬛
                            ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛
                            """)
                    .setFooter(getUseInfo())
                    .build()).queue();
            case 4 -> event.replyEmbeds(new EmbedBuilder()
                    .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                    .setColor(Color.WHITE)
                    .addField("Molar", ":tooth:", false)
                    .setFooter(getUseInfo())
                    .build()).queue();
            default -> event.reply("ㅁㄹ").queue();
        }
    }

}
