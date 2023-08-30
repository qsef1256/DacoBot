package net.qsef1256.dacobot.command.fun;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.qsef1256.dacobot.service.cmdstat.CmdStatistic;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class ZaraCommand extends SlashCommand {

    public ZaraCommand() {
        name = "자라";
        help = "자라는 거북목 자라과의 동물으로, 한반도 토종 거북입니다.";
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        CmdStatistic statistic = new CmdStatistic(getClass());

        String message = switch (LocalDateTime.now().getHour()) {
            case 22, 23, 0 -> "새 나라의 어린이는 일찍 잡니다.";
            case 1, 2 -> "부엉 부엉";
            case 4, 5, 6, 7 -> "부엉 부엉 부엉 부엉 부엉";
            case 8, 9, 10, 11 -> "시에스타?";
            default -> "근데 지금 자야 할 시간인가요?";
        };

        EmbedBuilder embedBuilder = DiaEmbed.primary("자라", message, null);
        embedBuilder.setImage("https://media.tenor.com/COj0Hup5pz8AAAAd/turtle.gif");
        embedBuilder.setFooter(statistic.getUseInfo());

        event.replyEmbeds(embedBuilder.build()).queue();
    }

}
