package net.qsef1256.dacobot.game.boardv2.omok;

import net.qsef1256.dacobot.game.boardv2.api.board.GameCoordinate;
import net.qsef1256.dacobot.game.boardv2.api.lifecycle.GameTurn;
import net.qsef1256.dacobot.game.boardv2.impl.board.EmojiPiece;
import net.qsef1256.dacobot.game.boardv2.impl.board.GridBoard;
import net.qsef1256.dacobot.game.boardv2.impl.board.XYCoordinate;
import net.qsef1256.dacobot.game.paint.enums.Emoji;
import org.jetbrains.annotations.NotNull;

import static net.qsef1256.dacobot.game.boardv2.omok.OmokTurn.BLACK;
import static net.qsef1256.dacobot.game.boardv2.omok.OmokTurn.WHITE;

public class OmokBoard extends GridBoard {

    public boolean isWin(@NotNull GameCoordinate coordinate, @NotNull GameTurn turn) {
        XYCoordinate xy = (XYCoordinate) coordinate;
        int x = xy.getX();
        int y = xy.getY();

        EmojiPiece piece = (EmojiPiece) turn.getPiece();

        if (hexagonWin(buildCheckString(x, y, 1, 0), piece)) return true;
        if (hexagonWin(buildCheckString(x, y, 0, 1), piece)) return true;
        if (hexagonWin(buildCheckString(x, y, 1, 1), piece)) return true;
        if (hexagonWin(buildCheckString(x, y, 1, -1), piece)) return true;

        return false;
    }

    private boolean hexagonWin(@NotNull String checkString, EmojiPiece stone) {
        if (checkString.contains(getNumMok(BLACK.getPiece(), 6))
                || checkString.contains(getNumMok(WHITE.getPiece(), 6))) return false;
        return checkString.contains(getNumMok(BLACK.getPiece(), 5)) && stone == BLACK.getPiece()
                || checkString.contains(getNumMok(WHITE.getPiece(), 5)) && stone == WHITE.getPiece();
    }

    @NotNull
    private static String getNumMok(@NotNull EmojiPiece piece, int count) {
        return String.valueOf(piece.getEmoji()).repeat(count);
    }

    @NotNull
    private String buildCheckString(int x, int y, int dx, int dy) {
        int checkLength = getWidth();
        int topY = y - dx * checkLength;
        int topX = x - dy * checkLength;

        Emoji[] stones = new Emoji[13];
        int count = 0;
        int destX = topX;
        int destY = topY;
        for (int i = 0; i < checkLength * 2 + 1; i++) {
            if (!isInBound(destX, destY)) {
                destY += dx;
                destX += dy;
                continue;
            }
            stones[count] = getGrid().getPixel(destX, destY);
            destY += dx;
            destX += dy;
            count++;
        }

        StringBuilder sb = new StringBuilder();
        for (Emoji pixelColor : stones) {
            sb.append(pixelColor != null ? pixelColor.getEmoji() : "");
        }

        return sb.toString();
    }

}
