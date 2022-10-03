package net.qsef1256.dacobot.command.fun;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dacobot.util.RandomUtil;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

public class TNTCommand extends SlashCommand {

    private static final double GRAM_TNT_TO_J = 4184;
    private static final double MC_TO_REAL = 0.36; // https://www.youtube.com/watch?v=FwFKiRsYTLs

    public TNTCommand() {
        name = "tnt";
        help = "마인크래프트 TNT n개의 폭발력은 얼마 정도일까요?";

        options = List.of(new OptionData(OptionType.INTEGER, "갯수", "폭발 시킬 TNT 갯수").setRequired(true));
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        OptionMapping option = event.getOption("갯수");
        if (option == null) {
            event.reply("메시지를 입력해주세요.").setEphemeral(true).queue();
            return;
        }

        User user = event.getUser();
        String result;

        double tntCount;
        try {
            tntCount = option.getAsDouble();
        } catch (NumberFormatException e) {
            event.replyEmbeds(DiaEmbed.error("폭발 실패", "올바른 정수를 입력하세요: %s".formatted(option.getAsString()), null, null).build()).queue();
            return;
        }

        String boom = ":boom: TNT %d개가 터졌습니다.".formatted(Math.round(tntCount));

        if (tntCount < 0) {
            result = boom + " :sparkles: 물리 법칙이 붕괴되어 모든 세계가 사라졌습니다...";
        } else if (tntCount == 0) {
            result = boom + " :dash: 아무 일도 일어나지 않았습니다.";
        } else if (tntCount == 1) {
            result = boom + " 블록 %d개가 날아갔습니다!".formatted(RandomUtil.randomInt(16, 29));
        } else if (tntCount < 10) {
            result = boom + " 블록 %d개가 날아갔습니다!".formatted(Math.round(tntCount * RandomUtil.randomInt(14, 25)));
        } else if (tntCount < 250) {
            result = boom + " 블록 %d개가 날아갔습니다! ".formatted(Math.round(tntCount * RandomUtil.randomInt(10, 19)))
                    + (RandomUtil.randomBool() ? "%s 도 잘못해서 휩쓸리고 말았습니다!".formatted(user.getName()) : "");
        } else if (tntCount < 25000) {
            result = boom + " 블록 몇개가 날아갔는진 모?루겠어요. %s는 폭사했습니다!".formatted(user.getName());
        } else {
            result = boom;
        }

        double ton = tntCount * MC_TO_REAL;
        double kt = ton / 1000;
        double Mt = kt / 1000;
        double J = kt * GRAM_TNT_TO_J * 1000;

        // https://en.wikipedia.org/wiki/Effects_of_nuclear_explosions
        // https://nuclearweaponarchive.org/Nwfaq/Nfaq5.html#nfaq5.1

        final double constantBlast = 0.71;
        final double constantRad = 0.7;
        double radBlast = Math.pow(kt, 0.33) * constantBlast;
        double radRadiation = Math.pow(kt, 0.19) * constantRad;

        double radThermal1st = Math.pow(kt, 0.38) * 1.20;
        double radThermal2nd = Math.pow(kt, 0.40) * 0.87;
        double radThermal3rd = Math.pow(kt, 0.41) * 0.67;

        // https://www.atomicarchive.com/resources/documents/effects/glasstone-dolan/chapter6.html#%C2%A76.72

        double blastCraterFeet = 30 * Math.pow(kt, 0.3);
        double blastCrater = blastCraterFeet / 3.281;

        EmbedBuilder embedBuilder = DiaEmbed.info("폭발 계산기", result, null);
        DecimalFormat df = new DecimalFormat("0.00000");

        String displayTon = (Mt > 1) ? "%s Mt".formatted(df.format(Mt)) : "%s kt".formatted(df.format(kt));
        embedBuilder.addField("예상 폭발력", displayTon + "%n%s J".formatted(Math.round(J)), true);
        embedBuilder.addField("폭발 범위(km)", String.valueOf(df.format(radBlast)), true);
        embedBuilder.addField("방사선 범위(km)", String.valueOf(df.format(radRadiation)), true);

        if (1 <= kt && Mt <= 20) {
            embedBuilder.addField("1도 화상(km)", String.valueOf(df.format(radThermal1st)), true);
            embedBuilder.addField("2도 화상(km)", String.valueOf(df.format(radThermal2nd)), true);
            embedBuilder.addField("3도 화상(km)", String.valueOf(df.format(radThermal3rd)), true);
        }
        embedBuilder.addField("크레이터 크기(m)", String.valueOf(blastCrater), true);
        embedBuilder.addField("출처", "[Link](https://www.youtube.com/watch?v=FwFKiRsYTLs), " +
                "[Link](https://nuclearweaponarchive.org/Nwfaq/Nfaq5.html#nfaq5.1) Copyright 1997. Carey Sublette" +
                "[Link](https://www.atomicarchive.com/resources/documents/effects/glasstone-dolan/chapter6.html#%C2%A76.72)", false);
        embedBuilder.setFooter("계산 결과는 정확하지 않습니다.");

        event.replyEmbeds(embedBuilder.build()).queue();
    }

}
