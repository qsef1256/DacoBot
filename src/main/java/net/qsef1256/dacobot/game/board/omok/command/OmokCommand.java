package net.qsef1256.dacobot.game.board.omok.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.core.ui.command.DacoCommand;
import net.qsef1256.dacobot.game.board.omok.model.OmokController;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.setting.constants.DiaImage;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class OmokCommand extends DacoCommand {

    public OmokCommand() {
        name = "오목";
        help = "13*13 소형 오목 게임";

        children = new SlashCommand[]{
                new GameRuleCommand(),
                new StartCommand(),
                new PlaceCommand(),
                new ResignCommand(),
                new PullCommand(),
                new LogCommand()
        };
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        callNeedSubCommand();
    }

    private static class GameRuleCommand extends DacoCommand {

        public GameRuleCommand() {
            name = "규칙";
            help = "규칙을 확인합니다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            event.replyEmbeds(new EmbedBuilder()
                    .setColor(DiaColor.MAIN_COLOR)
                    .setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL)
                    .setTitle("오목 규칙")
                    .addField("종류", "표준 고모쿠룰", false)
                    .addField("설명", "모든 착수가 허용됩니다. 단, 장목(육목 이상)은 무시됩니다. [링크](https://namu.wiki/w/%EC%98%A4%EB%AA%A9/%EB%A3%B0%EC%9D%98%20%EC%A2%85%EB%A5%98#s-2.1.2)", false)
                    .setFooter("렌주룰 등은 알고리즘 가져오면 고려해보겠...")
                    .build()).queue();
        }

    }

    private static class StartCommand extends DacoCommand {

        @Setter(onMethod_ = {@Autowired})
        private OmokController omokController;

        public StartCommand() {
            name = "시작";
            help = "새 오목 게임을 시작합니다.";

            options = List.of(
                    new OptionData(OptionType.USER, "상대", "대국을 신청할 유저")
            );
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            User userOption = getOptionUser("상대");
            if (userOption == null) return;

            User user = event.getUser();
            User oppositeUser;
            try {
                oppositeUser = userOption;
            } catch (IllegalArgumentException e) {
                event.reply("올바른 유저가 아닙니다: %s".formatted(userOption.toString())).queue();
                return;
            }

            try {
                omokController.requestGame(event.getChannel(), user, oppositeUser);

                event.reply(oppositeUser.getName() + " 에게 오목 요청을 보냈습니다.").setEphemeral(true).queue();
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error("오목 요청 실패", null, e, user).build()).queue();
            }
        }

    }

    private static class PlaceCommand extends DacoCommand {

        @Setter(onMethod_ = {@Autowired})
        private OmokController omokController;

        public PlaceCommand() {
            name = "놓기";
            help = "돌을 놓습니다.";

            options = List.of(
                    new OptionData(OptionType.INTEGER, "x", "가로"),
                    new OptionData(OptionType.INTEGER, "y", "세로")
            );
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            User user = event.getUser();
            OptionMapping optionX = event.getOption("x");
            OptionMapping optionY = event.getOption("y");

            if (optionX == null || optionY == null) {
                event.reply("x,y 값을 입력해주세요. (1<=x<=13) (1<=y<=13)").queue();
                return;
            }

            long x = optionX.getAsLong();
            long y = optionY.getAsLong();
            try {
                omokController.previewStone(user.getIdLong(), (int) x, (int) y);

                event.deferReply().queue(callback -> callback.deleteOriginal().queue());
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error("오목 요청 실패", null, e, user).build()).queue(callback ->
                        callback.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
            }
        }

    }

    private static class ResignCommand extends DacoCommand {

        @Setter(onMethod_ = {@Autowired})
        private OmokController omokController;

        public ResignCommand() {
            name = "기권";
            help = "진행 중인 오목 게임을 기권하고 중단합니다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

            try {
                omokController.resign(user.getIdLong());

                event.reply("오목을 기권했습니다.").queue();
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error("오목 요청 실패", null, e, user).build()).queue();
            }
        }

    }

    private static class PullCommand extends DacoCommand {

        @Setter(onMethod_ = {@Autowired})
        private OmokController omokController;

        public PullCommand() {
            name = "끌올";
            help = "바둑판을 다시 띄웁니다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            User user = event.getUser();
            MessageChannel channel = event.getChannel();

            try {
                omokController.pullBoard(user.getIdLong(), channel);

                event.deferReply().queue(callback -> callback.deleteOriginal().queue());
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error("오목 요청 실패", null, e, user).build()).queue();
            }
        }

    }

    private static class LogCommand extends DacoCommand {

        @Setter(onMethod_ = {@Autowired})
        private OmokController omokController;

        public LogCommand() {
            name = "로그";
            help = "어디다가 뒀더라... 상대의 이전 수를 표시합니다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

            try {
                omokController.prevStone(user.getIdLong());

                event.deferReply().queue(callback -> callback.deleteOriginal().queue());
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error("오목 요청 실패", null, e, user).build()).queue(callback ->
                        callback.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
            }
        }

    }

}
