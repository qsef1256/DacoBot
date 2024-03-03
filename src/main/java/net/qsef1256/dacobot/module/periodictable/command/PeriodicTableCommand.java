package net.qsef1256.dacobot.module.periodictable.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.core.ui.command.DacoCommand;
import net.qsef1256.dacobot.module.periodictable.entity.Element;
import net.qsef1256.dacobot.module.periodictable.entity.ElementRepository;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dacobot.util.ParseUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class PeriodicTableCommand extends DacoCommand {

    public PeriodicTableCommand(@NotNull ShowTableCommand tableCommand,
                                @NotNull SearchCommand searchCommand) {
        name = "주기율표";
        help = "주기율표를 띄웁니다.";

        children = new SlashCommand[]{
                tableCommand,
                searchCommand
        };
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        callNeedSubCommand();
    }

    @Component
    public static class ShowTableCommand extends DacoCommand {

        public ShowTableCommand() {
            name = "보기";
            help = "전체 주기율표를 확인합니다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            String url = "https://ko.wikipedia.org/wiki/%EC%A3%BC%EA%B8%B0%EC%9C%A8%ED%91%9C#/media/%ED%8C%8C%EC%9D%BC:Simple_Periodic_Table_Chart-en.svg";

            event.reply(url).queue();
        }

    }

    @Component
    public static class SearchCommand extends DacoCommand {

        private final ElementRepository repository;

        public SearchCommand(@NotNull ElementRepository repository) {
            this.repository = repository;

            name = "검색";
            help = "원소를 찾습니다.";

            options = Collections.singletonList(new OptionData(OptionType.STRING, "검색어", "번호/기호/이름"));
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            String searchText = getOptionString("검색어");
            if (searchText == null) return;

            Element result = null;

            if (ParseUtil.canInteger(searchText))
                result = repository.findByNumber(Integer.parseInt(searchText));
            if (result == null) result = repository.findBySymbol(searchText);
            if (result == null) result = repository.findByName(searchText);
            if (result == null) result = repository.findByAlias(searchText);

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

    }

}
