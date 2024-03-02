package net.qsef1256.dacobot.game.hangman.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.core.ui.command.DacoCommand;
import net.qsef1256.dacobot.game.hangman.container.HangManContainer;
import net.qsef1256.dacobot.game.hangman.domain.HangMan;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dacobot.util.JDAUtil;
import net.qsef1256.dialib.util.CommonUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HangManCommand extends DacoCommand {

    public HangManCommand() {
        name = "행맨";
        help = "행맨 게임을 플레이 합니다.";

        children = new SlashCommand[]{
                new StartCommand(),
                new CustomStartCommand(),
                new ShowCommand(),
                new InputCommand()
        };
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        callNeedSubCommand();
    }

    public static class StartCommand extends DacoCommand {

        public StartCommand() {
            name = "시작";
            help = "행맨 게임을 시작합니다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            event.reply(underConstruction()).queue();

            // https://www.ef.co.kr/english-world/english-vocabulary/top-1000-words/
            /* TODO
             String word = JPAUtil.getRandomRow("english_word", JpaController.getEntityManager());

             HangManContainer.setHangman(new HangMan(word));
             */
        }

    }

    public static class CustomStartCommand extends DacoCommand {

        public CustomStartCommand() {
            name = "커스텀";
            help = "직접 단어를 지정해 행맨 게임을 시작합니다.";

            options = List.of(
                    new OptionData(OptionType.STRING, "단어", "시작할 단어")
            );
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            OptionMapping option = JDAUtil.getOptionMapping(event, "단어");
            if (option == null) return;

            String word = option.getAsString();
            if (!CommonUtil.isBetween(3, word.length(), 15)) {
                event.reply("단어는 3 ~ 15글자 사이여야 합니다.").setEphemeral(true).queue();
                return;
            }

            HangManContainer.getInstance().setHangman(new HangMan(word));
            event.replyEmbeds(DiaEmbed.success("행맨 게임", "%s 단어로 행맨 게임을 시작합니다.".formatted(word), null).build())
                    .setEphemeral(true)
                    .queue();

            event.getChannel().sendMessage(event.getUser().getName() + " 님이 행맨 게임을 시작했습니다!").queue();
            event.getChannel().sendMessageEmbeds(getHangmanStatus().build()).queue();
        }

    }

    public static class ShowCommand extends DacoCommand {

        public ShowCommand() {
            name = "보기";
            help = "행맨 게임의 현재 상태를 봅니다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            event.replyEmbeds(getHangmanStatus().build()).queue();
        }

    }

    public static class InputCommand extends DacoCommand {

        public InputCommand() {
            name = "입력";
            help = "글자를 집어 넣습니다.";

            options = List.of(
                    new OptionData(OptionType.STRING, "글자", "입력할 글자")
            );
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            OptionMapping option = event.getOption("글자");
            if (option == null) {
                event.reply("글자를 입력해주세요.").setEphemeral(true).queue();
                return;
            }
            HangMan hangman = HangManContainer.getInstance().getHangman();

            String input = option.getAsString();
            try {
                if (hangman.inputWord(input)) {
                    event.replyEmbeds(DiaEmbed.success("성공!", input + "글자는 있었습니다.", null).build()).queue();
                } else {
                    event.replyEmbeds(DiaEmbed.fail("실패!", input + "글자는 없었습니다.", null).build()).queue();
                }
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error(null,
                        "게임이 이미 끝났거나, 유효하지 않은 글자입니다: " + input,
                        null,
                        null).build()).queue();
                return;
            }

            event.getChannel()
                    .sendMessageEmbeds(getHangmanStatus().build())
                    .queue();
        }

    }

    private static @NotNull EmbedBuilder getHangmanStatus() {
        HangMan hangman = HangManContainer.getInstance().getHangman();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(DiaColor.MAIN_COLOR);
        embedBuilder.setTitle("행맨 게임");
        embedBuilder.setDescription(HANGMAN_DISPLAY[hangman.getRemainLife()]);
        embedBuilder.addField("단어", hangman.isEnd() ?
                hangman.getOriginWord() :
                hangman.getDisplayWord('?'), true);

        embedBuilder.addField("남은 목숨", String.valueOf(hangman.getRemainLife()), true);
        return embedBuilder;
    }

    private static final String[] HANGMAN_DISPLAY = new String[]{"""
            ```
              +---+
              |   |
              |   O
              |  /|\\
              |  / \\
              |
            =========
            ```
            """, """
            ```
              +---+
              |   |
              |   O
              |  /|\\
              |  /
              |
            =========
            ```
            """, """
            ```
              +---+
              |   |
              |   O
              |  /|\\
              |
              |
            =========
            ```
            """, """
            ```
              +---+
              |   |
              |   O
              |  /|
              |
              |
            =========
            ```
            """, """
            ```
              +---+
              |   |
              |   O
              |   |
              |
              |
            =========
            ```
            """, """
            ```
              +---+
              |   |
              |   O
              |
              |
              |
            =========
            ```
            """, """
            ```
              +---+
              |   |
              |
              |
              |
              |
            =========
            ```
            """, """
            ```
              +---+
              |
              |
              |
              |
              |
            =========
            ```
            """};

}
