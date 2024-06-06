package net.qsef1256.dacobot.game.board.sudoku.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.core.ui.command.DacoCommand;
import net.qsef1256.dacobot.game.board.GameAPI;
import net.qsef1256.dacobot.game.board.sudoku.model.SudokuHost;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SudokuCommand extends DacoCommand {

    public SudokuCommand() {
        name = "스도쿠";
        help = "스도쿠 게임을 시작합니다.";

        children = new SlashCommand[]{
                new StartCommand(),
                new SeeCommand(),
                new PlaceCommand(),
                new ResetCommand(),
                new SolveCommand()
        };
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        callNeedSubCommand();
    }

    public static class StartCommand extends DacoCommand {

        public StartCommand() {
            name = "시작";
            help = "스도쿠 게임을 시작합니다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            SudokuHost sudoku = new SudokuHost(event.getUser());

            try {
                GameAPI.newGame(sudoku, event);
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error(null, e.getMessage(), null, event.getUser()).build()).queue();
            }
        }

    }

    public static class PlaceCommand extends DacoCommand {

        public PlaceCommand() {
            name = "놓기";
            help = "특정한 위치에 숫자를 놓습니다.";

            options = List.of(
                    new OptionData(OptionType.INTEGER, "x", "x"),
                    new OptionData(OptionType.INTEGER, "y", "y"),
                    new OptionData(OptionType.INTEGER, "숫자", "놓을 숫자")
            );
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            OptionMapping xOption = event.getOption("x");
            OptionMapping yOption = event.getOption("y");
            OptionMapping numOption = event.getOption("숫자");

            if (xOption == null || yOption == null || numOption == null) {
                event.replyEmbeds(DiaEmbed.error(null, "숫자를 입력해주세요.", null, event.getUser()).build()).queue();
                return;
            }

            long x = xOption.getAsLong();
            long y = yOption.getAsLong();
            long number = numOption.getAsLong();

            try {
                SudokuHost game = GameAPI.getGame(new SudokuHost(event.getUser()));
                game.getGame().place((int) x, (int) y, (byte) number);

                event.reply(game.getGame().getUI().getUIMessage().build()).queue();
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error(null, e.getMessage(), null, event.getUser()).build()).queue();
            }
        }

    }

    public static class SeeCommand extends DacoCommand {

        public SeeCommand() {
            name = "보기";
            help = "현재 스도쿠를 봅니다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            try {
                SudokuHost game = GameAPI.getGame(new SudokuHost(event.getUser()));

                event.reply(game.getGame().getUI().getUIMessage().build()).queue();
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error(null, e.getMessage(), null, event.getUser()).build()).queue();
            }
        }

    }

    public static class ResetCommand extends DacoCommand {

        public ResetCommand() {
            name = "지우기";
            help = "스도쿠를 엎습니다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            try {
                SudokuHost game = GameAPI.getGame(new SudokuHost(event.getUser()));
                game.resign(event.getUser().getIdLong());

                event.reply(game.getUIMessage().build()).queue();
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error(null, e.getMessage(), null, event.getUser()).build()).queue();
            }
        }

    }

    public static class SolveCommand extends DacoCommand {

        public SolveCommand() {
            name = "해결";
            help = "치트를 씁니다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            try {
                SudokuHost game = GameAPI.getGame(new SudokuHost(event.getUser()));
                boolean solved = game.getGame().getSudokuGame().solve();

                if (!solved)
                    event.reply("이 스도쿠는 해결 불가능 합니다.").queue();
                else
                    event.reply(game.getGame().getUI().getUIMessage().build()).queue();
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error(null, e.getMessage(), null, event.getUser()).build()).queue();
            }
        }

    }

}
