package net.qsef1256.dacobot.game.paint.model;

import net.qsef1256.dacobot.game.paint.command.PaintCommand;
import net.qsef1256.dacobot.game.paint.enums.ColorEmoji;
import net.qsef1256.dacobot.game.paint.enums.Emoji;
import net.qsef1256.dacobot.game.paint.model.painter.Painter;
import net.qsef1256.dacobot.game.paint.model.painter.PainterContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static net.qsef1256.dacobot.DacoBot.logger;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PainterTest {
    static Painter painter;

    @BeforeAll
    static void init() {
        painter = PainterContainer.getPainter(419761037861060619L);
        painter.resize(6, 10);

        painter.fill(ColorEmoji.YELLOW, 1, 1);

        painter.paintPixel(ColorEmoji.BLUE, 1, 2);
        painter.paintPixel(ColorEmoji.BLUE, 2, 2);

        List<Emoji> pixelColors = new ArrayList<>(PaintCommand.parsePixelColor("rrrrbbbb"));
        painter.paintColumn(pixelColors, 3);
        painter.fill(ColorEmoji.BROWN, 1, 1);
    }

    @Test
    void printToString() {
        assertDoesNotThrow(() -> logger.info(painter.printPallet()));
    }

    @Test
    void isInBound() {
        assertDoesNotThrow(() -> {
            for (int y = 1; y <= 10; y++) {
                for (int x = 1; x <= 6; x++) {
                    painter.getPixel(x, y);
                }
            }
        });
    }

}