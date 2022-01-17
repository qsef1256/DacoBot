package net.qsef1256.diabot.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.qsef1256.diabot.enums.DiaColor;
import net.qsef1256.diabot.enums.DiaImage;
import net.qsef1256.diabot.enums.DiaInfo;
import net.qsef1256.diabot.util.CommonUtil;

import java.util.Arrays;

public class CreditCommand extends SlashCommand {
    public CreditCommand() {
        this.name = "credit";
        this.help = "다이아 덩어리의 구성 성분";
    }

    @Override
    protected void execute(SlashCommandEvent event) {

        String ment = CommonUtil.getRandomElement(
                Arrays.asList("폭발은 예술이다!", "?", "연락처는 장식이다 카더라", "(할말 없음)", "멘트 추천은 본체한테 DM"));

        event.replyEmbeds(new EmbedBuilder()
                .setColor(DiaColor.MAIN_COLOR)
                .setTitle(DiaInfo.BOT_NAME + " Credits")
                .setDescription("본체에게서 떨어져 나온 다이아 덩어리")
                .setThumbnail(DiaImage.MAIN_THUMBNAIL)
                .addField("본체", String.join(", ", DiaInfo.AUTHOR), true)
                .addField("버전", "v" + DiaInfo.VERSION, true)
                .addField("라이브러리", "JDA: https://github.com/DV8FromTheWorld/JDA\n" + "Chewtils: https://github.com/Chew/JDA-Chewtils", false)
                .addField("연락처", "`qsef1256@naver.com`", false)
                .addField("", ment, false)
                .setFooter("provided by JDA v4.4.0_350")
                .build()).queue();
    }
}
