package net.qsef1256.dacobot.game.boardv2.impl.board;

import lombok.Getter;
import net.qsef1256.dacobot.game.boardv2.api.board.GameBoard;
import net.qsef1256.dacobot.game.boardv2.api.board.GameCoordinate;
import net.qsef1256.dacobot.game.boardv2.api.board.GamePiece;
import net.qsef1256.dacobot.game.paint.enums.ColorEmoji;
import net.qsef1256.dacobot.game.paint.model.painter.Painter;
import org.jetbrains.annotations.NotNull;

import javax.management.openmbean.KeyAlreadyExistsException;

public class GridBoard implements GameBoard {

    public static final ColorEmojiPiece BOARD = new ColorEmojiPiece(ColorEmoji.BROWN);
    public static final EmojiPiece PREVIEW = new EmojiPiece(ColorEmoji.GREEN);

    @Getter
    private final Painter grid;

    public GridBoard() {
        grid = new Painter(13, 13);
        reset();
    }

    @Override
    public GamePiece getPiece(@NotNull GameCoordinate coordinate) {
        XYCoordinate xy = (XYCoordinate) coordinate;

        return new EmojiPiece(grid.getPixel(xy.getX(), xy.getY()));
    }

    @Override
    public void setPiece(@NotNull GameCoordinate coordinate, @NotNull GamePiece piece) {
        XYCoordinate xy = (XYCoordinate) coordinate;
        EmojiPiece emoji = (EmojiPiece) piece;

        grid.paintPixel(emoji.getEmoji(), xy.getX(), xy.getY());
    }

    @Override
    public String getDisplay() {
        return grid.printPallet();
    }

    @Override
    public void reset() {
        grid.erasePallet();
        grid.fill(BOARD.getEmoji(), 1, 1);
    }

    private void checkStone(@NotNull XYCoordinate coordinate) {
        int x = coordinate.getX();
        int y = coordinate.getY();

        if (!isInBound(x, y))
            throw new IllegalArgumentException("잘못된 좌표입니다. 입력한 x: " + x + " 입력한 y: " + y);
        if (grid.getPixel(x, y) != BOARD.getEmoji())
            throw new KeyAlreadyExistsException("(%s, %s) 엔 이미 돌이 놓여져 있습니다!".formatted(x, y));
    }

    public boolean canSet(XYCoordinate coordinate) {
        checkStone(coordinate);

        return getPiece(coordinate) == null;
    }

    public int getWidth() {
        return grid.getWidth();
    }

    public int getHeight() {
        return grid.getHeight();
    }

    public boolean isInBound(int x, int y) {
        return grid.isInBound(x, y);
    }

}
