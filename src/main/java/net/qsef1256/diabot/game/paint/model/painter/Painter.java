package net.qsef1256.diabot.game.paint.model.painter;

import net.qsef1256.diabot.game.paint.data.PixelEntity;
import net.qsef1256.diabot.game.paint.enums.PixelColor;
import net.qsef1256.diabot.util.CommonUtil;
import net.qsef1256.diabot.util.MatrixUtil;

import java.util.ArrayList;
import java.util.List;

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

    public void paintPixel(int x, int y, PixelColor color) {
        if (!CommonUtil.isBetween(x, 1, getWidth()) || !CommonUtil.isBetween(y, 1, getHeight())) {
            throw new IllegalArgumentException("올바른 숫자를 입력하세요. (1<=x<=" + getWidth() + ") (1<=y<=" + getHeight() + "), 입력된 숫자: " + x + ", " + y);
        }
        pallet[y - 1][x - 1] = color;
    }

    public void paintColumn(int y, List<PixelColor> colorList) {
        if (!CommonUtil.isBetween(y, 1, getHeight())) {
            throw new IllegalArgumentException("올바른 숫자를 입력해주세요. (1<=y<=" + getHeight() + "), 입력된 숫자: " + y);
        }
        for (int x = 0; x < getWidth(); x++) {
            pallet[y - 1][x] = colorList.get(x);
        }
    }

    public void paintAll(List<PixelColor> colorList) {
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
    }

    public void resizeVertical(int amount) {
        pallet = new MatrixUtil<>(PixelColor.class).resizeVertical(pallet, amount + getHeight());
    }

    public void resizeHorizon(int amount) {
        pallet = new MatrixUtil<>(PixelColor.class).resizeHorizon(pallet, amount + getWidth());
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

    public void setPixels(List<PixelEntity> pixelList) {
        for (PixelEntity pixel : pixelList) {
            pallet[pixel.getY()][pixel.getX()] = pixel.getPixelColor() != null ? pixel.getPixelColor() : PixelColor.WHITE;
        }
    }

}
