package net.qsef1256.diabot.game.paint.model;

import net.qsef1256.diabot.game.paint.command.PaintCommand;
import net.qsef1256.diabot.game.paint.enums.PixelColor;
import net.qsef1256.diabot.game.paint.model.painter.Painter;
import net.qsef1256.diabot.game.paint.model.painter.PainterContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class PainterTest {
    static Painter painter;

    @BeforeAll
    static void init() {
        painter = PainterContainer.getPainter(419761037861060619L);
        painter.resize(6, 10);

        painter.paintPixel(PixelColor.BLUE, 0, 0);
        painter.paintPixel(PixelColor.BLUE, 0, 1);

        List<PixelColor> pixelColors = PaintCommand.parsePixelColor("rrrrbbbb");
        painter.paintColumn(pixelColors, 2);
        painter.fill(PixelColor.BROWN, 0, 0);

    }

    @Test
    void printToString() {
        System.out.println(painter.printToString());
    }

    @Test
    void isInBound() {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 6; x++) {
                painter.getPixel(x, y);
            }
        }
    }

}