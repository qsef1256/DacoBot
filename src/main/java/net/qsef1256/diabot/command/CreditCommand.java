package net.qsef1256.diabot.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.qsef1256.diabot.enums.DiaColor;
import net.qsef1256.diabot.enums.DiaImage;
import net.qsef1256.diabot.enums.DiaInfo;
import net.qsef1256.diabot.util.CommonUtil;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.Properties;

public class CreditCommand extends SlashCommand {
    public CreditCommand() {
        name = "정보";
        help = "다이아 덩어리의 구성 성분";
    }

    @Override
    protected void execute(final SlashCommandEvent event) {
        final Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("project.properties"));
        } catch (final IOException | RuntimeException e) {
            event.replyEmbeds(new EmbedBuilder()
                    .setColor(DiaColor.FAIL)
                    .setTitle("정보 확인 실패")
                    .setDescription("봇 정보 확인에 실패했습니다.")
                    .setFooter("문제가 계속 발생할 경우 관리자를 불러주세요.")
                    .build()).queue();
            e.printStackTrace();
            return;
        }

        final long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        final String message = CommonUtil.getRandomElement(
                Arrays.asList("폭발은 예술이다!", "흠...", "연락처는 장식이다 카더라", "(할말 없음)", "멘트 추천은 본체한테 DM"));

        final String formattedUptime = DurationFormatUtils.formatDurationHMS(uptime);
        final String name = properties.getProperty("name");
        final String version = properties.getProperty("version");

        event.replyEmbeds(new EmbedBuilder()
                .setColor(DiaColor.MAIN_COLOR)
                .setTitle(name + " Credits")
                .setDescription("본체에게서 떨어져 나온 다이아 덩어리")
                .setThumbnail(DiaImage.MAIN_THUMBNAIL)
                .addField("본체", String.join(", ", DiaInfo.AUTHOR), true)
                .addField("버전", "v" + version, true)
                .addField("시작일", DiaInfo.SINCE, true)
                .addField("라이브러리", "JDA: https://github.com/DV8FromTheWorld/JDA\n" + "Chewtils: https://github.com/Chew/JDA-Chewtils", false)
                .addField("연락처", "`qsef1256@naver.com`", true)
                .addField("가동 시간", formattedUptime, true)
                .addField("", message, false)
                .setFooter("provided by JDA v4.4.0_350")
                .build()).queue();
    }
}
