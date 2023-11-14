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
import net.qsef1256.dacobot.game.paint.data.PaintEntity;
import net.qsef1256.dacobot.game.paint.model.PaintManagerImpl;
import net.qsef1256.dacobot.game.paint.model.painter.Painter;
import net.qsef1256.dacobot.game.paint.model.painter.PainterContainer;
import net.qsef1256.dacobot.game.paint.model.painter.PixelPainter;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.setting.constants.DiaImage;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dacobot.ui.DiaMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GalleryCommand extends SlashCommand {

    public GalleryCommand() {
        name = "갤러리";
        help = "그림들을 저장하거나 로드합니다.";

        children = new SlashCommand[]{
                new ShowCommand(),
                new SaveCommand(),
                new LoadCommand(),
                new DeleteCommand(),
                new ListCommand()
        };
    }

    private static void printPaint(@NotNull SlashCommandEvent event, String paintName, @NotNull Painter painter) {
        event.replyEmbeds(new EmbedBuilder()
                .setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL)
                .setColor(DiaColor.MAIN_COLOR)
                .addField(paintName, painter.printPallet(), false)
                .setFooter("/갤러리 에서 확인하세요")
                .build()).queue();
    }

    private static void applyPainter(SlashCommandEvent event, User user, String paintName, PixelPainter painter) {
        try {
            PaintEntity paint = new PaintManagerImpl().getPaint(user.getIdLong(), paintName);
            painter.resize(paint.getXSize(), paint.getYSize());
            painter.setPixelEntities(paint.getPixels());

            printPaint(event, paintName, painter);
        } catch (IllegalArgumentException e) {
            event.replyEmbeds(DiaEmbed.error(null, null, e, user)
                    .setFooter("그림이 없거나 권한이 없을 수 있어요.")
                    .build()).queue();
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
            event.replyEmbeds(DiaEmbed.error(null, "그림을 저장하던 도중 문제가 발생했습니다.", e, user).build()).queue();
        }
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        SlashCommand[] children = getChildren();

        event.reply(DiaMessage.needSubCommand(children, event.getMember())).queue();
    }

    private static class ShowCommand extends SlashCommand {

        public ShowCommand() {
            name = "보기";
            help = "저장된 그림을 봅니다. 로드 하지는 않습니다.";

            options = List.of(
                    new OptionData(OptionType.STRING, "이름", "그림 이름 (<= 32자)")
            );
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            OptionMapping paintNameOption = event.getOption("이름");
            User user = event.getUser();

            if (paintNameOption == null) {
                event.reply("그림 이름을 입력해주세요!").queue();
                return;
            }

            String paintName = paintNameOption.getAsString();
            PixelPainter painter = new PixelPainter();

            applyPainter(event, user, paintName, painter);
        }

    }

    private static class SaveCommand extends SlashCommand {

        public SaveCommand() {
            name = "저장";
            help = "그림을 저장합니다. 문제가 있는 그림은 제재될 수 있습니다.";

            options = List.of(
                    new OptionData(OptionType.STRING, "이름", "그림 이름 (<= 32자)")
            );
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            OptionMapping paintNameOption = event.getOption("이름");
            User user = event.getUser();

            if (paintNameOption == null) {
                event.reply("그림 이름을 입력해주세요!").queue();
                return;
            }

            String paintName = paintNameOption.getAsString();
            if (paintName.length() > 32) {
                event.reply("그림 이름이 너무 깁니다. (<= 32자)").queue();
                return;
            }

            Painter painter = PainterContainer.getPainter(user.getIdLong());

            try {
                new PaintManagerImpl().save(user.getIdLong(), paintName);

                printPaint(event, paintName, painter);
            } catch (IllegalArgumentException e) {
                event.replyEmbeds(new EmbedBuilder()
                                .setTitle("중복된 이름")
                                .setColor(DiaColor.FAIL)
                                .setDescription("오류: " + e.getMessage() + "\n\n" + "그림을 덮어쓰기 할 수 있습니다.")
                                .addField("덮어 쓸 그림", paintName, false)
                                .setFooter("덮어쓰기 된 그림은 복구할 수 없습니다.")
                                .build())
                        .addActionRow(Button.danger("gallery_overwrite", "덮어쓰기"))
                        .setEphemeral(true)
                        .queue();
            } catch (RuntimeException e) {
                log.warn(e.getMessage());
                event.replyEmbeds(DiaEmbed.error(null, "그림을 저장하던 도중 문제가 발생했습니다.", e, user).build()).queue();
            }
        }

    }

    private static class DeleteCommand extends SlashCommand {

        public DeleteCommand() {
            name = "삭제";
            help = "그림을 삭제합니다. 복구할 수 없습니다.";

            options = List.of(
                    new OptionData(OptionType.STRING, "이름", "그림 이름")
            );
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            OptionMapping paintNameOption = event.getOption("이름");
            User user = event.getUser();

            if (paintNameOption == null) {
                event.reply("그림 이름을 입력해주세요!").queue();
                return;
            }
            String paintName = paintNameOption.getAsString();

            try {
                new PaintManagerImpl().delete(user.getIdLong(), paintName);

                event.replyEmbeds(DiaEmbed.success("삭제 성공", paintName + "그림이 성공적으로 삭제 되었습니다.", user)
                        .setFooter("하드 용량 절약 성공")
                        .build()).queue();
            } catch (IllegalArgumentException e) {
                event.replyEmbeds(DiaEmbed.fail("삭제 실패", paintName + "그림을 삭제 하는데 실패 했습니다: " + e.getMessage(), user)
                        .setFooter("하드 용량 절약 실패")
                        .build()).queue();
            } catch (RuntimeException e) {
                log.warn(e.getMessage());
                event.replyEmbeds(DiaEmbed.error(null, "그림을 삭제하던 도중 문제가 발생했습니다.", e, user).build()).queue();
            }
        }

    }

    private static class LoadCommand extends SlashCommand {

        public LoadCommand() {
            name = "로드";
            help = "저장된 그림을 팔레트에 로드합니다.";

            options = List.of(
                    new OptionData(OptionType.STRING, "이름", "그림 이름")
            );
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            OptionMapping paintNameOption = event.getOption("이름");
            User user = event.getUser();

            if (paintNameOption == null) {
                event.reply("그림 이름을 입력해주세요!").queue();
                return;
            }

            String paintName = paintNameOption.getAsString();
            PixelPainter painter = (PixelPainter) PainterContainer.getPainter(user.getIdLong());

            applyPainter(event, user, paintName, painter);
        }

    }

    private static class ListCommand extends SlashCommand {

        public ListCommand() {
            name = "목록";
            help = "저장한 그림 목록을 찾습니다.";
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

            List<String> paintNames = new PaintManagerImpl().getOwnedPaint(user.getIdLong());

            event.replyEmbeds(new EmbedBuilder()
                    .setColor(DiaColor.INFO)
                    .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                    .setTitle(user.getName() + " 의 그림 목록")
                    .setDescription(String.join(", ", paintNames))
                    .build()).queue();
        }

    }

}
