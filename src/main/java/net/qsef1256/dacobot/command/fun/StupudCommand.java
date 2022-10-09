package net.qsef1256.dacobot.command.fun;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.qsef1256.dacobot.service.cmdstat.CmdStatistic;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dialib.util.RandomUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class StupudCommand extends SlashCommand {

    public StupudCommand() {
        name = "댕청";
        help = "I'm stupud";
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        if (event.getMember() == null) return;
        User user = event.getUser();
        CmdStatistic statistic = new CmdStatistic(getClass());

        final String message = RandomUtil.getRandomElement(
                Arrays.asList("I'm stupud", "나는 바보다", "I'M STUPUD", "멍멍", "하하하ㅏ하", "**나는 댕청하다**"));

        event.replyEmbeds(new EmbedBuilder()
                .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                .setTitle("나는 댕청하다")
                .setColor(DiaColor.MAIN_COLOR)
                .setDescription(message)
                .setFooter("바보 스택을 쌓은 횟수: " + statistic.getUseCount() + " 금일: " + statistic.getTodayUsed())
                .build()).queue();
    }

}
