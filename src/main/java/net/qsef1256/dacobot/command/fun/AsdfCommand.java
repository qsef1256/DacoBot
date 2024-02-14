package net.qsef1256.dacobot.command.fun;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.qsef1256.dacobot.command.DacoCommand;
import net.qsef1256.dacobot.setting.constants.DiaImage;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import net.qsef1256.dacobot.util.ColorUtil;
import net.qsef1256.dialib.util.RandomUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AsdfCommand extends DacoCommand {

    public AsdfCommand() {
        name = "ㅁㄴㅇㄹ";
        help = "asdf";

        statistic = true;
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        final String message = RandomUtil.getRandomElement(
                Arrays.asList("마늘요리", "ㅁㄴㅇㄹ", "마니오리", "마늘우림", "마녀오리", "마녀이름", "마니아러", "미나어롷", "므느으르",
                        "뫼니에르", "모닝알람", "매너어림", "만능요리", "마니와라", "무녀유령", "마늘오리", "매니아로", "마나오링",
                        "마나의룬", "모난오리"));

        event.replyEmbeds(new EmbedBuilder()
                .setColor(ColorUtil.randomRainbow())
                .setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL)
                .setDescription(message)
                .setFooter(getUseInfo())
                .build()).queue();
    }

}
