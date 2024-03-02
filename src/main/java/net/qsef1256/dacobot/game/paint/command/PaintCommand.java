package net.qsef1256.dacobot.game.paint.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.qsef1256.dacobot.core.ui.command.DacoCommand;
import net.qsef1256.dacobot.game.paint.enums.ColorEmoji;
import net.qsef1256.dacobot.game.paint.enums.Emoji;
import net.qsef1256.dacobot.game.paint.model.PaintDrawer;
import net.qsef1256.dacobot.game.paint.model.painter.Painter;
import net.qsef1256.dacobot.game.paint.model.painter.PainterContainer;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.setting.constants.DiaImage;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class PaintCommand extends DacoCommand {

    public PaintCommand(@NotNull ShowCommand showCommand,
                        @NotNull PixelCommand pixelCommand,
                        @NotNull ColorCommand colorCommand,
                        @NotNull ColumnCommand columnCommand,
                        @NotNull DrawAllCommand drawAllCommand,
                        @NotNull ResizeCommand resizeCommand,
                        @NotNull EraseCommand eraseCommand,
                        @NotNull FillCommand fillCommand,
                        @NotNull DrawerCommand drawerCommand) {
        name = "그림";
        help = "이모지 그림을 그립니다. 그림은 공유됩니다.";

        children = new SlashCommand[]{
                showCommand,
                pixelCommand,
                colorCommand,
                columnCommand,
                drawAllCommand,
                resizeCommand,
                eraseCommand,
                fillCommand,
                drawerCommand
        };
    }

    private static void printPallet(@NotNull SlashCommandEvent event, Painter paint) {
        User user = event.getUser();

        try {
            event.replyEmbeds(new EmbedBuilder()
                    .setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL)
                    .setColor(DiaColor.MAIN_COLOR)
                    .addField(user.getName() + "의 팔레트", paint.printPallet(), false)
                    .setFooter("/갤러리 저장 으로 그림을 저장하세요.")
                    .build()).queue();
        } catch (RuntimeException e) {
            log.warn(e.getMessage());

            event.replyEmbeds(new EmbedBuilder()
                    .setColor(DiaColor.FAIL)
                    .setTitle("오류 발생")
                    .setDescription("그림을 보여주는 도중 문제가 발생했습니다.")
                    .setFooter("문제가 계속될 시 관리자를 불러주세요.")
                    .build()).queue();
        }
    }

    @NotNull
    public static List<ColorEmoji> parsePixelColor(@NotNull String colorString) {
        List<ColorEmoji> colorList = new ArrayList<>();

        if (colorString.isEmpty())
            throw new IllegalArgumentException("빈 문자열은 색깔 이모지로 변환할 수 없습니다.");

        try {
            colorList.add(ColorEmoji.valueOf(colorString.toUpperCase()));
            return colorList;
        } catch (IllegalArgumentException ignored) {
            // ignored
        }

        for (char character : colorString.toCharArray()) {
            if (character == ' ') continue;
            if (ColorEmoji.findById(String.valueOf(character)) == null)
                throw new IllegalArgumentException("알 수 없는 색깔 키 입니다: " + character);
            colorList.add(ColorEmoji.findById(String.valueOf(character)));
        }

        return colorList;
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        callNeedSubCommand();
    }

    @Component
    public static class ShowCommand extends DacoCommand {

        public ShowCommand() {
            name = "보기";
            help = "그림을 봅니다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            User user = event.getUser();
            Painter paint = PainterContainer.getPainter(user.getIdLong());

            printPallet(event, paint);
        }

    }

    @Component
    public static class PixelCommand extends DacoCommand {

        public PixelCommand() {
            name = "찍기";
            help = "지정된 좌표에 점을 찍습니다.";

            options = List.of(
                    new OptionData(OptionType.INTEGER, "x", "가로"),
                    new OptionData(OptionType.INTEGER, "y", "세로"),
                    new OptionData(OptionType.STRING, "색깔", "색깔")
            );
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            OptionMapping optionX = event.getOption("x");
            OptionMapping optionY = event.getOption("y");
            OptionMapping optionColor = event.getOption("색깔");

            User user = event.getUser();
            Painter painter = PainterContainer.getPainter(user.getIdLong());

            if (optionX == null || optionY == null) {
                event.reply("x,y 값을 입력해주세요. (1<=x<=" + painter.getWidth() + ") (1<=y<=" + painter.getHeight() + ")").queue();
                return;
            }
            if (optionColor == null) {
                event.reply("색깔 또는 색깔 ID를 입력해주세요. /그림 색깔").queue();
                return;
            }
            long x = optionX.getAsLong();
            long y = optionY.getAsLong();
            Emoji color;

            try {
                color = parsePixelColor(optionColor.getAsString()).get(0);

                painter.paintPixel(color, (int) x, (int) y);
            } catch (IllegalArgumentException e) {
                event.reply("오류 발생: %s".formatted(e.getMessage())).queue();
                return;
            } catch (RuntimeException e) {
                log.warn(e.getMessage());
                event.replyEmbeds(DiaEmbed.error(null, "그림을 그리던 도중 문제가 발생했습니다.", null, user).build()).queue();
                return;
            }

            printPallet(event, painter);
        }

    }

    @Component
    public static class ColumnCommand extends DacoCommand {

        public ColumnCommand() {
            name = "줄";
            help = "한 줄을 입력합니다.";

            options = List.of(
                    new OptionData(OptionType.INTEGER, "번호", "줄 번호"),
                    new OptionData(OptionType.STRING, "내용", "줄을 색깔 ID에 따라 입력해주세요.")
            );
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            OptionMapping optionY = event.getOption("번호");
            OptionMapping optionColumn = event.getOption("내용");

            User user = event.getUser();
            Painter painter = PainterContainer.getPainter(user.getIdLong());

            if (optionY == null) {
                event.reply("줄 번호를 입력해주세요. (1<=y<=" + painter.getHeight() + ")").queue();
                return;
            }
            if (optionColumn == null) {
                event.reply("내용을 입력해주세요.").queue();
                return;
            }
            long y = optionY.getAsLong();
            String column = optionColumn.getAsString();

            if (column.length() > painter.getWidth()) {
                event.reply("입력한 행이 너무 깁니다. 현재 행 길이: " + painter.getWidth() + " 입력한 길이: " + column.length()).queue();
                return;
            }

            try {
                List<Emoji> colorList = new ArrayList<>(parsePixelColor(column));

                painter.paintColumn(colorList, (int) y);
            } catch (IllegalArgumentException e) {
                event.reply("오류 발생: %s".formatted(e.getMessage())).queue();
                return;
            } catch (RuntimeException e) {
                log.warn(e.getMessage());
                event.replyEmbeds(DiaEmbed.error(null, "그림을 그리던 도중 문제가 발생했습니다.", null, user).build()).queue();
                return;
            }

            printPallet(event, painter);
        }

    }

    @Component
    public static class DrawAllCommand extends DacoCommand {

        public DrawAllCommand() {
            name = "한줄로";
            help = "한 줄로 그림을 그립니다. 가독성을 위해 띄어쓸 수 있습니다.";

            options = List.of(new OptionData(OptionType.STRING,
                    "내용",
                    "줄을 색깔 ID에 따라 입력해주세요. 스페이스가 허용되지만 무시됩니다.")
            );
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            OptionMapping optionColumn = event.getOption("내용");
            if (optionColumn == null) {
                event.reply("내용을 입력해주세요.").queue();
                return;
            }

            User user = event.getUser();
            Painter painter = PainterContainer.getPainter(user.getIdLong());

            String column = optionColumn.getAsString();

            try {
                List<Emoji> colorList;
                colorList = new ArrayList<>(parsePixelColor(column));

                painter.paintAll(colorList);
            } catch (IllegalArgumentException e) {
                event.reply("오류 발생: %s".formatted(e.getMessage())).queue();
                return;
            } catch (RuntimeException e) {
                log.warn(e.getMessage());
                event.replyEmbeds(DiaEmbed.error(null, "그림을 그리던 도중 문제가 발생했습니다.", null, user).build()).queue();
                return;
            }

            printPallet(event, painter);
        }

    }

    @Component
    public static class EraseCommand extends DacoCommand {

        public EraseCommand() {
            name = "지우개";
            help = "쓱쓱싹싹 다컴의 용량을 아낍시다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            User user = event.getUser();
            Painter painter = PainterContainer.getPainter(user.getIdLong());

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL)
                    .setColor(DiaColor.WARNING)
                    .setTitle("아까운데...")
                    .addField("지울 그림", painter.printPallet() + "\n정말 지우실 껀가요? 복구할 수 없어요.", false)
                    .setFooter("지우고 나면 이거보다 멋진 그림을 그려주세요.");

            event.replyEmbeds(embedBuilder.build())
                    .setEphemeral(true)
                    .addActionRow(Button.danger("paint_erase", "지우기"))
                    .queue();
        }

    }

    @Component
    public static class ResizeCommand extends DacoCommand {

        public ResizeCommand() {
            name = "크기";
            help = "그림의 크기를 바꿉니다.";

            options = List.of(
                    new OptionData(OptionType.INTEGER, "x", "x 크기 (0 < x)"),
                    new OptionData(OptionType.INTEGER, "y", "y 크기 (0 < y)")
            );
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            OptionMapping optionX = event.getOption("x");
            OptionMapping optionY = event.getOption("y");

            User user = event.getUser();
            Painter painter = PainterContainer.getPainter(user.getIdLong());

            if (optionX == null || optionY == null) {
                event.reply("x와 y를 입력해주세요. (0 < x,y)").queue();
                return;
            }

            long x = optionX.getAsLong();
            long y = optionY.getAsLong();
            if (0 >= x || 0 >= y) {
                event.reply("그림 크기는 0보다 커야 합니다.").queue();
                return;
            }
            if (x > 26 || y > 26) {
                event.reply("그림 크기가 너무 큽니다! 그림 크기는 26보다 작아야 합니다.").queue();
                return;
            }

            try {
                painter.resize((int) x, (int) y);
            } catch (IllegalArgumentException e) {
                event.reply("오류 발생: %s".formatted(e.getMessage())).queue();
                return;
            } catch (RuntimeException e) {
                log.warn(e.getMessage());
                event.replyEmbeds(DiaEmbed.error(null, "크기를 바꾸던 도중 문제가 발생했습니다.", null, user).build()).queue();
                return;
            }

            printPallet(event, painter);
        }

    }

    @Component
    public static class FillCommand extends DacoCommand {

        public FillCommand() {
            name = "채우기";
            help = "페인트 통, 지정된 색깔로 바꿔줍니다.";

            options = List.of(
                    new OptionData(OptionType.INTEGER, "x", "x 좌표"),
                    new OptionData(OptionType.INTEGER, "y", "y 좌표"),
                    new OptionData(OptionType.STRING, "색깔", "색깔")
            );
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            OptionMapping optionX = event.getOption("x");
            OptionMapping optionY = event.getOption("y");
            OptionMapping optionColor = event.getOption("색깔");

            User user = event.getUser();
            Painter painter = PainterContainer.getPainter(user.getIdLong());

            if (optionX == null || optionY == null) {
                event.reply("x와 y를 입력해주세요. (0 < x,y)").queue();
                return;
            }
            if (optionColor == null) {
                event.reply("색깔 또는 색깔 ID를 입력해주세요. /그림 색깔").queue();
                return;
            }

            long x = optionX.getAsLong();
            long y = optionY.getAsLong();
            Emoji color;

            try {
                color = parsePixelColor(optionColor.getAsString()).get(0);

                painter.fill((int) x, (int) y, color);
            } catch (IllegalArgumentException e) {
                event.reply("오류 발생: " + e.getMessage()).queue();
                return;
            } catch (RuntimeException e) {
                log.warn(e.getMessage());
                event.replyEmbeds(new EmbedBuilder()
                        .setColor(DiaColor.FAIL)
                        .setTitle("오류 발생")
                        .setDescription("그림을 그리던 도중 문제가 발생했습니다.")
                        .setFooter("문제가 계속될 시 관리자를 불러주세요.")
                        .build()).queue();
                return;
            }

            printPallet(event, painter);
        }

    }

    @Component
    public static class DrawerCommand extends DacoCommand {

        private final PaintDrawer paintDrawer;

        public DrawerCommand(@NotNull PaintDrawer paintDrawer) {
            this.paintDrawer = paintDrawer;

            name = "그림판";
            help = "그림판을 띄웁니다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            paintDrawer.initDrawer(event);
        }

    }

    @Component
    public static class ColorCommand extends DacoCommand {

        public ColorCommand() {
            name = "색깔";
            help = "색깔 목록을 확인합니다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ColorEmoji color : ColorEmoji.values()) {
                stringBuilder.append(String.format("%s %s(**%s**)%n",
                        color.getEmoji(),
                        color.name().toLowerCase(),
                        color.getId()));
            }

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL)
                    .setColor(DiaColor.INFO)
                    .setTitle("색깔 목록(ID)")
                    .setDescription(stringBuilder.toString());
            event.replyEmbeds(embedBuilder.build()).queue();
        }

    }

}
