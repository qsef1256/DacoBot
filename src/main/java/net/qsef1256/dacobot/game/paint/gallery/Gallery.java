package net.qsef1256.dacobot.game.paint.gallery;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.game.paint.entity.PaintEntity;
import net.qsef1256.dacobot.game.paint.model.PaintController;
import net.qsef1256.dacobot.game.paint.model.painter.Painter;
import net.qsef1256.dacobot.game.paint.model.painter.PixelPainter;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.setting.constants.DiaImage;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

// TODO: change scope of gallery (include all UI Embed?)
@Slf4j
@Component
public class Gallery {

    private final PaintController paintController;

    public Gallery(@NotNull PaintController paintController) {
        this.paintController = paintController;
    }

    @NotNull
    public MessageEmbed getPaint(@NotNull String paintName,
                                 @NotNull Painter painter) {
        return new EmbedBuilder()
                .setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL)
                .setColor(DiaColor.MAIN_COLOR)
                .addField(paintName, painter.printPallet(), false)
                .setFooter("/갤러리 에서 확인하세요")
                .build();
    }

    @NotNull
    public MessageEmbed applyPainter(@NotNull User user,
                                     @Nullable String paintName,
                                     @NotNull PixelPainter painter) {
        if (paintName == null)
            return DiaEmbed.fail("그림 이름을 입력해주세요!", null, user).build();
        if (paintName.length() > 32)
            return DiaEmbed.fail("그림 이름이 너무 깁니다. (<= 32자)", null, user).build();

        try {
            PaintEntity paint = paintController.getPaint(user.getIdLong(), paintName);
            painter.resize(paint.getXSize(), paint.getYSize());
            painter.setPixelEntities(paint.getPixels());

            return getPaint(paintName, painter);
        } catch (IllegalArgumentException e) {
            return DiaEmbed.error(null, null, e, user)
                    .setFooter("그림이 없거나 권한이 없을 수 있어요.")
                    .build();
        } catch (RuntimeException e) {
            log.warn(e.getMessage());

            return DiaEmbed.error(null, "그림을 저장하던 도중 문제가 발생했습니다.", e, user).build();
        }
    }

}
