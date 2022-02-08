package net.qsef1256.diabot.game.paint.model.painter;

import net.qsef1256.diabot.game.paint.data.PixelEntity;
import net.qsef1256.diabot.game.paint.enums.PixelColor;
import net.qsef1256.diabot.util.MatrixUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

// x and y from 1
public class Painter {

    private PixelColor[][] pallet;

    public Painter() {
        this(8, 8);
    }

    public Painter(int xSize, int ySize) {
        pallet = new PixelColor[xSize][ySize];
        initPallet();
    }

    private void initPallet() {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (pallet[y][x] == null) pallet[y][x] = PixelColor.WHITE;
            }
        }
    }

    public int getWidth() {
        return new MatrixUtil<>(PixelColor.class).getWidth(pallet);
    }

    public int getHeight() {
        return new MatrixUtil<>(PixelColor.class).getHeight(pallet);
    }

    public void erasePallet() {
        pallet = new PixelColor[getHeight()][getWidth()];
    }

    public String printToString() {
        initPallet();
        StringBuilder output = new StringBuilder();
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                output.append(pallet[y][x].getEmoji());
            }
            output.append("\n");
        }
        return output.toString();
    }

    public void paintPixel(PixelColor color, int x, int y) {
        validate(x, y);
        pallet[y - 1][x - 1] = color;
    }

    public void paintColumn(List<PixelColor> colorList, int y) {
        if (!isInBound(1, y)) {
            throw new IllegalArgumentException("올바른 숫자를 입력해주세요. (1<=y<=" + getHeight() + "), 입력된 숫자: " + y);
        }
        for (int x = 0; x < getWidth(); x++) {
            pallet[y - 1][x] = colorList.get(x);
        }
    }

    public void paintAll(@NotNull List<PixelColor> colorList) {
        if (colorList.size() == 0) {
            throw new IllegalArgumentException("칠할 게 없네요!");
        }
        int i = 0;
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (i >= colorList.size()) return;

                pallet[y][x] = colorList.get(i);
                i++;
            }
        }
    }

    public void resize(int x, int y) {
        pallet = new MatrixUtil<>(PixelColor.class).resize(pallet, x, y);
        initPallet();
    }

    public void resizeVertical(int amount) {
        pallet = new MatrixUtil<>(PixelColor.class).resizeVertical(pallet, amount + getHeight());
        initPallet();
    }

    public void resizeHorizon(int amount) {
        pallet = new MatrixUtil<>(PixelColor.class).resizeHorizon(pallet, amount + getWidth());
        initPallet();
    }

    public PixelColor getPixel(int x, int y) {
        validate(x, y);

        return pallet[y - 1][x - 1];
    }

    public List<PixelEntity> getPixels() {
        List<PixelEntity> pixelList = new ArrayList<>();

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                pixelList.add(new PixelEntity()
                        .setX(x)
                        .setY(y)
                        .setPixelColor(pallet[y][x]));
            }
        }
        return pixelList;
    }

    public void setPixels(@NotNull List<PixelEntity> pixelList) {
        for (PixelEntity pixel : pixelList) {
            pallet[pixel.getY()][pixel.getX()] = pixel.getPixelColor() != null ? pixel.getPixelColor() : PixelColor.WHITE;
        }
    }

    private void fill(int x, int y, PixelColor targetColor, PixelColor replacementColor) {
        if (!isInBound(x, y)) return;
        if (targetColor == replacementColor) return;
        if (pallet[y - 1][x - 1] != targetColor) return;
        pallet[y - 1][x - 1] = replacementColor;
        fill(x + 1, y, targetColor, replacementColor);
        fill(x - 1, y, targetColor, replacementColor);
        fill(x, y + 1, targetColor, replacementColor);
        fill(x, y - 1, targetColor, replacementColor);
    }

    public void fill(PixelColor replacementColor, int x, int y) {
        validate(x, y);
        fill(x, y, pallet[y - 1][x - 1], replacementColor);
    }

    public boolean isInBound(int x, int y) {
        return 1 <= x && x <= getWidth() && 1 <= y && y <= getHeight();
    }

    private void validate(int x, int y) {
        if (!isInBound(x, y)) {
            throw new IllegalArgumentException("올바른 숫자를 입력하세요. (1<=x<=" + getWidth() + ") (1<=y<=" + getHeight() + "), 입력된 숫자: " + x + ", " + y);
        }
    }

}
