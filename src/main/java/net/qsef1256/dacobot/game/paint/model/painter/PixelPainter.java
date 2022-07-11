package net.qsef1256.dacobot.game.paint.model.painter;

import net.qsef1256.dacobot.game.paint.data.PixelEntity;
import net.qsef1256.dacobot.game.paint.enums.ColorEmoji;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * PixelEntity 용 페인터
 *
 * @see Painter
 */
public class PixelPainter extends Painter {

    public PixelPainter() {
        this(8, 8);
    }

    public PixelPainter(int xSize, int ySize) {
        super(xSize, ySize);
        initPallet();
    }

    public List<PixelEntity> getPixelEntities() {
        List<PixelEntity> pixelList = new ArrayList<>();

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                pixelList.add(new PixelEntity()
                        .setX(x)
                        .setY(y)
                        .setPixelColor((ColorEmoji) pallet[y][x]));
            }
        }
        return pixelList;
    }

    public void setPixelEntities(@NotNull List<PixelEntity> pixelList) {
        for (PixelEntity pixel : pixelList) {
            pallet[pixel.getY()][pixel.getX()] = pixel.getPixelColor() != null ? pixel.getPixelColor() : ColorEmoji.WHITE;
        }
    }

}
