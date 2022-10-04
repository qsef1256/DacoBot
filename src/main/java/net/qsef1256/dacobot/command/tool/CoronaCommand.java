package net.qsef1256.dacobot.command.tool;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.qsef1256.dacobot.service.openapi.corona.CoronaApi;
import net.qsef1256.dacobot.service.openapi.corona.CoronaEntity;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dacobot.util.LocalDateTimeUtil;

public class CoronaCommand extends SlashCommand {

    public CoronaCommand() {
        name = "코로나";
        help = "오늘자 대한민국 코로나 상황을 확인합니다.";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        try {
            CoronaEntity coronaData = CoronaApi.getData();
            if (coronaData == null) throw new IllegalArgumentException("코로나 데이터가 null 입니다.");

            event.replyEmbeds(DiaEmbed.info("코로나 현황", null, null)
                    .addField("총 확진자", String.valueOf(coronaData.getDecideCnt()), true)
                    .addField("총 사망자", String.valueOf(coronaData.getDeathCnt()), true)
                    .addField("\u200B", "\u200B", false)
                    .addField("오늘 확진자", String.valueOf(coronaData.getAddDecide()), true)
                    .addField("오늘 사망자", String.valueOf(coronaData.getAddDeath()), true)
                    .addField("업데이트 시간", LocalDateTimeUtil.getTimeString(coronaData.getUpdateTime()), false)
                    .setFooter("provided by 보건복지부")
                    .build()).queue();
        } catch (RuntimeException e) {
            event.replyEmbeds(DiaEmbed.error(null, "코로나 API에 접속하던 중 문제가 발생했습니다.", e, null).build()).queue();
        }
    }

}
