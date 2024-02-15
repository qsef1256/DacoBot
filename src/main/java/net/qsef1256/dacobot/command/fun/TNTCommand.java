package net.qsef1256.dacobot.command.fun;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.command.DacoCommand;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dacobot.util.JDAUtil;
import net.qsef1256.dialib.util.MathUtil;
import net.qsef1256.dialib.util.RandomUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Component
public class TNTCommand extends DacoCommand {

    private static final double GRAM_TNT_TO_J = 4184;
    private static final double MC_TO_REAL = 0.36; // https://www.youtube.com/watch?v=FwFKiRsYTLs

    public TNTCommand() {
        name = "tnt";
        help = "마인크래프트 TNT n개의 폭발력은 얼마 정도일까요?";

        options = List.of(new OptionData(OptionType.INTEGER, "갯수", "폭발 시킬 TNT 갯수").setRequired(true));
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        OptionMapping option = JDAUtil.getOptionMapping(event, "갯수");
        if (option == null) return;

        User user = event.getUser();
        String result;
        double tntCount;
        try {
            tntCount = option.getAsDouble();
        } catch (NumberFormatException e) {
            event.replyEmbeds(DiaEmbed.error("폭발 실패", "올바른 정수를 입력하세요: %s".formatted(option.getAsString()),
                    null, null).build()).queue();
            return;
        }

        String boom = ":boom: TNT %d개가 터졌습니다.".formatted(Math.round(tntCount));

        result = switchTntCondition(tntCount)
                .caseCondition(tnt -> tnt < 0, () -> "%s :sparkles: 물리 법칙이 붕괴되어 모든 세계가 사라졌습니다..."
                        .formatted(boom))
                .caseCondition(tnt -> tnt == 0, () -> "%s :dash: 아무 일도 일어나지 않았습니다."
                        .formatted(boom))
                .caseCondition(tnt -> tnt == 1, () -> "%s 블록 %d개가 날아갔습니다!"
                        .formatted(boom, RandomUtil.randomInt(16, 29)))
                .caseCondition(tnt -> tnt < 10, () -> "%s 블록 %d개가 날아갔습니다!"
                        .formatted(boom, Math.round(tntCount * RandomUtil.randomInt(14, 25))))
                .caseCondition(tnt -> tnt < 250, () -> "%s 블록 %d개가 날아갔습니다! "
                        .formatted(boom, Math.round(tntCount * RandomUtil.randomInt(10, 19)))
                        + (RandomUtil.randomBool() ? "%s 도 잘못해서 휩쓸리고 말았습니다!"
                        .formatted(user.getName()) : ""))
                .caseCondition(tnt -> tnt < 25000, () -> "%s 블록 몇개가 날아갔는진 모?루겠어요. %s는 폭사했습니다!"
                        .formatted(boom, user.getName()))
                .caseCondition(tnt -> tnt < 100000, () -> "%s %s는 폭사했습니다!"
                        .formatted(boom, user.getName())
                        + (RandomUtil.randomBool() ? "주변에 있던 %s의 집도 날아갔습니다!"
                        .formatted(user.getName()) : ""))
                .caseCondition(tnt -> tnt < 1000000, () -> "%s 주변 바이옴이 날아갔습니다! %s는 폭사했습니다!"
                        .formatted(boom, user.getName()))
                .caseCondition(tnt -> tnt < 1500000, () -> "%s 응답 시간 초과로 서버가 날아갔습니다! %s는 폭사하기 전에 튕겼습니다!"
                        .formatted(boom, user.getName()))
                .caseCondition(tnt -> tnt < 2500000, () -> ("%s :computer::fire_extinguisher: 서버를 돌리던 컴퓨터가 폭발했습니다! " +
                        "%s는 폭사하기 전에 튕겼습니다! 수리비로 %s 만원이 지출 되었습니다.")
                        .formatted(boom, user.getName(), MathUtil.floorDigit(RandomUtil.randomInt(600000, 2500000), 5)))
                .caseCondition(tnt -> tnt < 10000000, () -> ("%s :house_abandoned::fire_engine::ambulance: " +
                        "서버를 돌리던 집이 폭발했습니다! %s는 폭사했습니다! 장례 및 수습비로 %s 만원이 지출 되었습니다.")
                        .formatted(boom, user.getName(), MathUtil.floorDigit(RandomUtil.randomInt(2500000, 350000000), 5)))
                .caseCondition(tnt -> tnt < 70000000, () -> ("%s :cityscape::rotating_light: %s가 살던 도시가 폭발했습니다! " +
                        "%s 명의 사상자가 발생했고 국가비상사태가 선포되었습니다!")
                        .formatted(boom, user.getName(), RandomUtil.randomInt(25, 5000)))
                .caseCondition(tnt -> tnt < 250000000, () -> ("%s :flag_kr::rotating_light: %s가 살던 국가가 폭발했습니다! " +
                        "%s 명의 사상자가 발생했고 국가비상사태가 선포되었습니다!")
                        .formatted(boom, user.getName(), RandomUtil.randomInt(2500, 5000000))) // TODO: regular distribution?
                .caseCondition(tnt -> tnt < 750000000, () -> "%s :volcano: %s가 살던 대륙이 폭발했습니다! 전 지구적 재앙이 발생해 인류 문명이 붕괴했습니다!"
                        .formatted(boom, user.getName()))
                .caseCondition(tnt -> tnt < 1700000000, () -> "%s :earth_asia: 지구가 폭발했습니다! 폭발한 지구의 잔해가 태양계에 새로운 소행성대를 만들었습니다!"
                        .formatted(boom))
                .caseCondition(tnt -> tnt < 2100000000, () -> "%s :fireworks: 우주가 폭발했습니다! 새로운 빅뱅이 탄생했습니다!"
                        .formatted(boom))
                .caseCondition(tnt -> tnt == Integer.MAX_VALUE, () -> "%s Integer가 폭발했습니다! 오버플로우가 일어났습니다! 하지만 다코봇은 안전합니다."
                        .formatted(boom))
                .defaultResult(() -> boom);


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
        embedBuilder.addField("예상 폭발력", displayTon + "%n%s J".

                formatted(Math.round(J)), true);
        embedBuilder.addField("폭발 범위(km)", String.valueOf(df.format(radBlast)), true);
        embedBuilder.addField("방사선 범위(km)", String.valueOf(df.format(radRadiation)), true);

        if (1 <= kt && Mt <= 20) {
            embedBuilder.addField("1도 화상(km)", String.valueOf(df.format(radThermal1st)), true);
            embedBuilder.addField("2도 화상(km)", String.valueOf(df.format(radThermal2nd)), true);
            embedBuilder.addField("3도 화상(km)", String.valueOf(df.format(radThermal3rd)), true);
        }
        embedBuilder.addField("크레이터 크기(m)", String.valueOf(blastCrater), true);
        embedBuilder.addField("출처", "[Link](https://www.youtube.com/watch?v=FwFKiRsYTLs), " +
                "[Link](https://nuclearweaponarchive.org/Nwfaq/Nfaq5.html#nfaq5.1) Copyright 1997. Carey Sublette, " +
                "[Link](https://www.atomicarchive.com/resources/documents/effects/glasstone-dolan/chapter6.html#%C2%A76.72)", false);
        embedBuilder.setFooter("계산 결과는 정확하지 않습니다.");

        event.replyEmbeds(embedBuilder.build()).

                queue();
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    private ResultSwitch<Double, String> switchTntCondition(double tnt) {
        return new ResultSwitch<>(tnt);
    }

    // TODO: move to DiaLib? is enough generic way?
    private static class ResultSwitch<T, R> {

        private final T value;
        private R result;

        public ResultSwitch(@NotNull T value) {
            this.value = value;
        }

        public ResultSwitch<T, R> caseCondition(@NotNull Predicate<T> condition,
                                                @NotNull Supplier<R> resultSupplier) {
            if (result == null && condition.test(value))
                result = resultSupplier.get();

            return this;
        }

        public R defaultResult(Supplier<R> resultSupplier) {
            if (result == null)
                result = resultSupplier.get();

            return result;
        }

    }

}
