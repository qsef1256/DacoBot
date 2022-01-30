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
        painter.paintPixel(1,1, PixelColor.BLUE);
        painter.paintPixel(1,2, PixelColor.BLUE);

        List<PixelColor> pixelColors = PaintCommand.parsePixelColor("rrrrbbbb");
        painter.paintColumn(3,pixelColors);
        painter.resize(1,1);

    }

    @Test
    void printToString() {
        System.out.println(painter.printToString());
    }

    @Test
    void save() {
    }

    @Test
    void load() {
    }

}