package net.qsef1256.dacobot.command.help;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.core.command.DacoCommand;
import net.qsef1256.dacobot.setting.constants.DiaImage;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dacobot.util.JDAUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.NoSuchElementException;

@Slf4j
@Component
public class HelpCommand extends DacoCommand {

    @Autowired
    public HelpCommand(@NotNull MainMenuCommand menuCommand,
                       @NotNull FindCommand findCommand) {
        name = "도움말";
        help = "다이아 덩어리를 다루는 방법";

        children = new SlashCommand[]{
                menuCommand,
                findCommand
        };
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        callNeedSubCommand();
    }

    @Component
    public static class MainMenuCommand extends DacoCommand {

        private final DiaHelp diaHelp;

        @Autowired
        public MainMenuCommand(@Lazy DiaHelp diaHelp) {
            this.diaHelp = diaHelp;

            name = "전체";
            help = "도움말의 메인 카테고리를 확인합니다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            Member member = event.getMember();
            if (member == null) return;

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL);

            try {
                event.replyEmbeds(diaHelp.getMainMenu(member).build()).queue();
            } catch (RuntimeException e) {
                log.error("failed to load DiaHelp", e);

                event.replyEmbeds(DiaEmbed.error(null,
                        "설정 파일을 로드하는데 실패했습니다.",
                        null,
                        null).build()).queue();
            }
        }

    }

    @Component
    public static class FindCommand extends DacoCommand {

        private final DiaHelp diaHelp;

        @Autowired
        public FindCommand(@Lazy DiaHelp diaHelp) {
            this.diaHelp = diaHelp;

            name = "찾기";
            help = "특정 카테고리의 도움말을 찾습니다.";

            options = Collections.singletonList(new OptionData(OptionType.STRING,
                    "카테고리",
                    "찾을 카테고리").setRequired(true));
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            Member member = event.getMember();
            if (member == null) return;

            final OptionMapping option = JDAUtil.getOptionMapping(event, "카테고리");
            if (option == null) return;

            try {
                event.replyEmbeds(diaHelp.getSearchResult(member, option.getAsString()).build()).queue();
            } catch (NoSuchElementException e) {
                event.replyEmbeds(DiaEmbed.fail("찾기 실패",
                                option.getAsString() + " 카테고리는 존재하지 않아요!",
                                null)
                        .setFooter("/도움말 전체 로 카테고리 목록을 확인하세요.")
                        .build()).queue();
            } catch (RuntimeException e) {
                log.warn(e.getMessage());

                event.replyEmbeds(DiaEmbed.error(null,
                        option.getAsString() + " 카테고리 로딩중 오류가 발생했어요.",
                        null,
                        null).build()).queue();
            }
        }

    }

}
