package net.qsef1256.dacobot.game.paint.model;

import net.qsef1256.dacobot.game.paint.command.PaintCommand;
import net.qsef1256.dacobot.game.paint.enums.PixelColor;
import net.qsef1256.dacobot.game.paint.model.painter.Painter;
import net.qsef1256.dacobot.game.paint.model.painter.PainterContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class PainterTest {
    static Painter painter;

    @BeforeAll
    static void init() {
        painter = PainterContainer.getPainter(419761037861060619L);
        painter.resize(6, 10);

        painter.paintPixel(PixelColor.BLUE, 1, 2);
        painter.paintPixel(PixelColor.BLUE, 2, 2);

        List<PixelColor> pixelColors = PaintCommand.parsePixelColor("rrrrbbbb");
        painter.paintColumn(pixelColors, 3);
        painter.fill(PixelColor.BROWN, 1, 1);

    }

    @Test
    void printToString() {
        System.out.println(painter.printToString());
    }

    @Test
    void isInBound() {
        for (int y = 1; y <= 10; y++) {
            for (int x = 1; x <= 6; x++) {
                painter.getPixel(x, y);
            }
        }
    }

}