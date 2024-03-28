package net.qsef1256.dacobot.command.fun;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.core.ui.command.DacoCommand;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dialib.util.RandomUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class StupudCommand extends DacoCommand {

    public StupudCommand() {
        name = "댕청";
        help = "I'm stupud";

        statistic = true;
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        if (event.getMember() == null) return;
        User user = event.getUser();
        String message = RandomUtil.getRandomElement(
                Arrays.asList("I'm stupud", "나는 바보다", "I'M STUPUD", "멍멍", "하하하ㅏ하", "**나는 댕청하다**"));

        event.replyEmbeds(new EmbedBuilder()
                .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                .setTitle("나는 댕청하다")
                .setColor(DiaColor.MAIN_COLOR)
                .setDescription(message)
                .setFooter("바보 스택을 쌓은 횟수: %d 금일: %d".formatted(
                        getCmdStatistic().getUseCount(),
                        getCmdStatistic().getTodayUsed()))
                .build()).queue();
    }

}
