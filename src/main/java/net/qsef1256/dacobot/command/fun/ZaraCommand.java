package net.qsef1256.dacobot.command.fun;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.qsef1256.dacobot.core.ui.command.DacoCommand;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dialib.util.RandomUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ZaraCommand extends DacoCommand {

    public ZaraCommand() {
        name = "자라";
        help = "자라는 거북목 자라과의 동물으로, 한반도 토종 거북입니다.";

        statistic = true;
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        String message = switch (LocalDateTime.now().getHour()) {
            case 22, 23, 0 -> "새 나라의 어린이는 일찍 잡니다.";
            case 1, 2 -> "부엉 부엉";
            case 4, 5, 6, 7 -> "부엉 부엉 부엉 부엉 부엉";
            case 8, 9, 10, 11 -> "시에스타?";
            default -> "근데 지금 자야 할 시간인가요?";
        };

        EmbedBuilder embedBuilder = DiaEmbed.primary("자라", message, null);
        switch (RandomUtil.randomInt(1, 2)) {
            case 1 -> embedBuilder.setImage("https://media.tenor.com/COj0Hup5pz8AAAAd/turtle.gif");
            case 2 -> embedBuilder.setImage(
                    "https://media.discordapp.net/attachments/949632236691529768/1146469838789808138/1545116962330.jpg");
            default -> {
                // do nothing
            }
        }

        embedBuilder.setFooter(getUseInfo());
        event.replyEmbeds(embedBuilder.build()).queue();
    }

}
