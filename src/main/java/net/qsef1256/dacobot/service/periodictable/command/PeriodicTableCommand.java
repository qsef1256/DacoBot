package net.qsef1256.dacobot.service.periodictable.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.database.DaoCommonJpa;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;
import net.qsef1256.dacobot.service.periodictable.entity.Element;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dacobot.ui.DiaMessage;
import net.qsef1256.dacobot.util.JDAUtil;
import net.qsef1256.dacobot.util.ParseUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PeriodicTableCommand extends SlashCommand {

    private static final DaoCommonJpa<Element, Integer> elementDao = new DaoCommonJpaImpl<>(Element.class);

    public PeriodicTableCommand() {
        name = "주기율표";
        help = "주기율표를 띄웁니다.";

        children = new SlashCommand[]{
                new ShowTableCommand(),
                new SearchCommand()
        };
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        DiaMessage.needSubCommand(getChildren(), event.getMember());

        event.reply(DiaMessage.needSubCommand(getChildren(), event.getMember())).queue();
    }

    public static class ShowTableCommand extends SlashCommand {

        public ShowTableCommand() {
            name = "보기";
            help = "전체 주기율표를 확인합니다.";
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            event.reply("https://ko.wikipedia.org/wiki/%EC%A3%BC%EA%B8%B0%EC%9C%A8%ED%91%9C#/media/%ED%8C%8C%EC%9D%BC:Simple_Periodic_Table_Chart-en.svg").queue();
        }

    }

    public static class SearchCommand extends SlashCommand {

        public SearchCommand() {
            name = "검색";
            help = "원소를 찾습니다.";

            options = Collections.singletonList(new OptionData(OptionType.STRING, "검색어", "번호/기호/이름"));
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            OptionMapping option = JDAUtil.getOptionMapping(event, "검색어");
            if (option == null) return;

            String searchText = option.getAsString();

            elementDao.open();
            Element result = null;

            if (ParseUtil.canInteger(searchText)) result = search(Map.of("number", Integer.valueOf(searchText)));
            if (result == null) result = search(Map.of("symbol", searchText));
            if (result == null) result = search(Map.of("name", searchText));
            if (result == null) result = search(Map.of("alias", searchText));
            elementDao.close();

            if (result == null) {
                event.replyEmbeds(DiaEmbed.fail("검색 실패", "%s (번) 원소를 찾지 못했어요.".formatted(searchText), null).build()).queue();
            } else {
                event.replyEmbeds(new EmbedBuilder()
                        .setTitle("%s(%s)".formatted(result.getName(), result.getEngName()))
                        .setColor(result.getElementSeries().getColor())
                        .appendDescription("%s족 %s주기 ".formatted(result.getElementGroup(), result.getElementPeriod()))
                        .appendDescription(result.getDescription() != null ? result.getDescription() : "")
                        .addField("원자 번호", String.valueOf(result.getNumber()), true)
                        .addField("원소 기호", result.getSymbol(), true)
                        .addField("계열", result.getElementSeries().getName(), true)
                        .addField("원자량", String.valueOf(result.getWeight()), true)
                        .addField("밀도", String.valueOf(result.getDensity()), true)
                        .addField("녹는점", String.valueOf(result.getMelting()), true)
                        .addField("끓는점", String.valueOf(result.getBoiling()), true)
                        .addField("상태", result.getPhaseOnSATP().getName(), true)
                        .addField("생성 원인", result.getGenerationCause().getName(), true)
                        .build()).queue();
            }
        }

        @Nullable
        private static Element search(Map<String, Object> searchText) {
            List<Element> results = elementDao.findBy(searchText);

            Element result = null;
            if (!results.isEmpty()) result = results.get(0);
            return result;
        }

    }

}
