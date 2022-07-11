package net.qsef1256.dacobot.game.paint.model.painter;

import lombok.Getter;
import net.qsef1256.dacobot.game.paint.enums.ColorEmoji;
import net.qsef1256.dacobot.game.paint.enums.Emoji;
import net.qsef1256.dacobot.util.MatrixUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class Painter {

    public static final int X_START = 1;
    public static final int Y_START = 1;
    @Getter
    protected Emoji[][] pallet;

    public Painter(int xSize, int ySize) {
        pallet = new Emoji[xSize][ySize];
        initPallet();
    }

    protected void initPallet() {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (pallet[y][x] == null) pallet[y][x] = ColorEmoji.WHITE;
            }
        }
    }

    public int getWidth() {
        return new MatrixUtil<>(Emoji.class).getWidth(pallet);
    }

    public int getHeight() {
        return new MatrixUtil<>(Emoji.class).getHeight(pallet);
    }

    public void erasePallet() {
        pallet = new Emoji[getHeight()][getWidth()];
    }

    public String printPallet() {
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

    public void paintPixel(Emoji color, int x, int y) {
        validate(x, y);
        pallet[y - Y_START][x - X_START] = color;
    }

    public void paintColumn(List<Emoji> colorList, int y) {
        if (!isInBound(1, y)) {
            throw new IllegalArgumentException("올바른 숫자를 입력해주세요. (1<=y<=" + getHeight() + "), 입력된 숫자: " + y);
        }
        for (int x = 0; x < getWidth(); x++) {
            pallet[y - Y_START][x] = colorList.get(x);
        }
    }

    public void paintAll(@NotNull List<Emoji> colorList) {
        if (colorList.isEmpty()) {
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
        pallet = new MatrixUtil<>(Emoji.class).resize(pallet, x, y);
        initPallet();
    }

    public void resizeVertical(int amount) {
        pallet = new MatrixUtil<>(Emoji.class).resizeVertical(pallet, amount + getHeight());
        initPallet();
    }

    public void resizeHorizon(int amount) {
        pallet = new MatrixUtil<>(Emoji.class).resizeHorizon(pallet, amount + getWidth());
        initPallet();
    }

    public Emoji getPixel(int x, int y) {
        validate(x, y);

        return pallet[y - Y_START][x - X_START];
    }

    public List<Emoji> getPixels() {
        return new MatrixUtil<>(Emoji.class).toList(pallet);
    }

    private void fill(int x, int y, Emoji targetColor, Emoji replacementColor) {
        if (!isInBound(x, y)) return;
        if (targetColor == replacementColor) return;
        if (pallet[y - Y_START][x - X_START] != targetColor) return;
        pallet[y - Y_START][x - X_START] = replacementColor;
        fill(x + 1, y, targetColor, replacementColor);
        fill(x - 1, y, targetColor, replacementColor);
        fill(x, y + 1, targetColor, replacementColor);
        fill(x, y - 1, targetColor, replacementColor);
    }

    public void fill(Emoji replacementColor, int x, int y) {
        validate(x, y);
        fill(x, y, pallet[y - Y_START][x - X_START], replacementColor);
    }

    public boolean isInBound(int x, int y) {
        return X_START <= x && x <= getWidth() && Y_START <= y && y <= getHeight();
    }

    private void validate(int x, int y) {
        if (!isInBound(x, y)) {
            throw new IllegalArgumentException("올바른 숫자를 입력하세요. (1<=x<=" + getWidth() + ") (1<=y<=" + getHeight() + "), 입력된 숫자: " + x + ", " + y);
        }
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(pallet);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;

        return o.hashCode() == this.hashCode();
    }

    @Override
    public String toString() {
        return super.toString() + "[width=" + getWidth() + ", height=" + getHeight() + "]";
    }

}
