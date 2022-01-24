package net.qsef1256.diabot.command.fun;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.qsef1256.diabot.enums.DiaColor;
import net.qsef1256.diabot.enums.DiaImage;
import net.qsef1256.diabot.model.CmdStatistic;
import net.qsef1256.diabot.util.CommonUtil;

import java.util.Arrays;

public class StupudCommand extends SlashCommand {

    public StupudCommand() {
        name = "stupud";
        help = "I'm stupud";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        CmdStatistic statistic = new CmdStatistic(getClass());

        final String message = CommonUtil.getRandomElement(
                Arrays.asList("I'm stupud", "나는 바보다", "I'M STUPUD", "멍멍", "하하하ㅏ하", "다시 한번 얘기하지만 나는 댕청하다"));

        event.replyEmbeds(new EmbedBuilder()
                .setAuthor("qsef1256", null, DiaImage.MAIN_THUMBNAIL)
                .setTitle("나는 댕청하다")
                .setColor(DiaColor.MAIN_COLOR)
                .setDescription(message)
                .setFooter("바보 스택을 쌓은 횟수: " + statistic.getUseCount() + " 금일: " + statistic.getTodayUsed())
                .build()).queue();
    }
}
