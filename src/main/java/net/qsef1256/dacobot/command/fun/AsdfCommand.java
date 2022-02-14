package net.qsef1256.dacobot.command.fun;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.qsef1256.dacobot.enums.DiaImage;
import net.qsef1256.dacobot.enums.DiaInfo;
import net.qsef1256.dacobot.model.CmdStatistic;
import net.qsef1256.dacobot.util.ColorUtil;
import net.qsef1256.dacobot.util.CommonUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class AsdfCommand extends SlashCommand {

    public AsdfCommand() {
        name = "ㅁㄴㅇㄹ";
        help = "asdf";
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {

        CmdStatistic statistic = new CmdStatistic(getClass());

        final String message = CommonUtil.getRandomElement(
                Arrays.asList("마늘요리", "ㅁㄴㅇㄹ", "마니알아", "마니오리", "마늘우림", "마녀오리", "모노레일", "마녀이름", "마니아러"
                        , "미나어롷", "므느으르", "뫼니에르", "모닝알람", "매너어림", "만능요리", "마니와라", "무녀유령", "마늘오리", "매니아로", "마나오링", "마나의룬", "모난오리"));

        event.replyEmbeds(new EmbedBuilder()
                .setColor(ColorUtil.randomRainbow())
                .setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL)
                .setDescription(message)
                .setFooter(statistic.getUseInfo())
                .build()).queue();
    }

}