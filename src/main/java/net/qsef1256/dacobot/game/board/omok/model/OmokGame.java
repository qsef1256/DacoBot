package net.qsef1256.dacobot.game.board.omok.model;

import lombok.Getter;
import net.qsef1256.dacobot.game.board.model.BoardGame;
import net.qsef1256.dacobot.game.paint.enums.ColorEmoji;
import net.qsef1256.dacobot.game.paint.enums.Emoji;
import net.qsef1256.dacobot.game.paint.model.painter.Painter;
import net.qsef1256.dialib.util.CommonUtil;
import org.jetbrains.annotations.NotNull;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.concurrent.atomic.AtomicBoolean;

public class OmokGame implements BoardGame<ColorEmoji> {

    public static final ColorEmoji BOARD = ColorEmoji.BROWN;
    public static final ColorEmoji BLACK = ColorEmoji.BLACK;
    public static final ColorEmoji WHITE = ColorEmoji.WHITE;
    public static final ColorEmoji PREVIEW = ColorEmoji.GREEN;

    @Getter
    private final Painter painter;
    @Getter
    private boolean isBlackTurn;
    @Getter
    private OmokStatus status;

    private int prevX;
    private int prevY;
    private int previewX;
    private int previewY;
    private Emoji previewColor;

    public OmokGame() {
        painter = new Painter(13, 13);
        reset();
    }

    @NotNull
    private static String getNumMok(@NotNull ColorEmoji color, int count) {
        return String.valueOf(color.getEmoji()).repeat(count);
    }

    @Override
    public String printBoard() {
        return painter.printPallet();
    }

    public void prevStone(ColorEmoji stone) {
        checkTurn(stone);
        if (prevX == 0 || prevY == 0) throw new IllegalStateException("저장된 로그가 없습니다.");

        resetPreview();
        previewColor = painter.getPixel(prevX, prevY);
        painter.paintPixel(PREVIEW, prevX, prevY);
        status = OmokStatus.PREV;
    }

    public void previewStone(int x, int y, ColorEmoji stone) {
        checkTurn(stone);
        resetPreview();
        checkStone(x, y, stone);

        previewColor = painter.getPixel(x, y);
        previewX = x;
        previewY = y;
        painter.paintPixel(PREVIEW, x, y);
        status = OmokStatus.PREVIEW;
    }

    @Override
    public void place(int x, int y, ColorEmoji stone) {
        checkTurn(stone);
        resetPreview();
        checkStone(x, y, stone);

        prevX = x;
        prevY = y;
        processStone(stone, x, y);
        isBlackTurn = !isBlackTurn;
    }

    public void confirmStone(ColorEmoji stone) {
        if (status != OmokStatus.PREVIEW)
            throw new IllegalStateException(OmokStatus.PREVIEW.getDisplay() + " 상태가 아닙니다.");

        place(previewX, previewY, stone);
    }

    public void resetPreview() {
        if (isEnd()) return;
        if (status == OmokStatus.PREVIEW) painter.paintPixel(previewColor, previewX, previewY);
        if (status == OmokStatus.PREV) painter.paintPixel(previewColor, prevX, prevY);

        status = OmokStatus.WAIT;
        previewColor = null;
    }

    private ColorEmoji getStone() {
        return isBlackTurn ? OmokGame.BLACK : OmokGame.WHITE;
    }

    private void processStone(ColorEmoji stone, int x, int y) {
        painter.paintPixel(stone, x, y);
        if (checkWin(buildCheckString(x, y, 1, 0), stone)) win(stone);
        if (checkWin(buildCheckString(x, y, 0, 1), stone)) win(stone);
        if (checkWin(buildCheckString(x, y, 1, 1), stone)) win(stone);
        if (checkWin(buildCheckString(x, y, 1, -1), stone)) win(stone);
        if (checkDraw()) draw();
    }

    private void checkStone(int x, int y, ColorEmoji stone) {
        if (!painter.isInBound(x, y))
            throw new IllegalArgumentException("잘못된 좌표입니다. 입력한 x: " + x + " 입력한 y: " + y);
        if (stone != BLACK && stone != WHITE)
            throw new IllegalArgumentException("잘못된 돌 입니다: " + stone);
        if (painter.getPixel(x, y) != BOARD)
            throw new KeyAlreadyExistsException("(%s, %s) 엔 이미 돌이 놓여져 있습니다!".formatted(x, y));
    }

    private void checkTurn(ColorEmoji stone) {
        if (isEnd())
            throw new IllegalStateException("이미 게임이 끝났습니다. 현재 상태: " + status.getDisplay());
        if (getStone() != stone)
            throw new IllegalStateException("당신의 차례가 아닙니다. 현재 차례: " + (isBlackTurn ? "흑돌" : "백돌"));
    }

    @NotNull
    private String buildCheckString(int x, int y, int dx, int dy) {
        int checkLength = painter.getWidth();
        int topY = y - dx * checkLength;
        int topX = x - dy * checkLength;

        Emoji[] stones = new Emoji[13];
        int count = 0;
        int destX = topX;
        int destY = topY;
        for (int i = 0; i < checkLength * 2 + 1; i++) {
            if (!painter.isInBound(destX, destY)) {
                destY += dx;
                destX += dy;
                continue;
            }
            stones[count] = painter.getPixel(destX, destY);
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

    private boolean checkWin(@NotNull String checkString, ColorEmoji stone) {
        if (checkString.contains(getNumMok(BLACK, 6)) || checkString.contains(getNumMok(WHITE, 6))) return false;
        return checkString.contains(getNumMok(BLACK, 5)) && stone == BLACK || checkString.contains(getNumMok(WHITE, 5)) && stone == WHITE;
    }

    private boolean checkDraw() {
        AtomicBoolean anyEmpty = new AtomicBoolean(false);
        painter.getPixels().forEach(pixel -> {
            if (pixel == OmokGame.BOARD) anyEmpty.set(true);
        });
        return !anyEmpty.get();
    }

    private void win(ColorEmoji stone) {
        status = (stone == WHITE) ? OmokStatus.WHITE_WIN : OmokStatus.BLACK_WIN;
    }

    private void draw() {
        status = OmokStatus.DRAW;
    }

    public void resign(ColorEmoji stone) {
        status = (stone == WHITE) ? OmokStatus.WHITE_RESIGN : OmokStatus.BLACK_RESIGN;
    }

    public void reset() {
        status = OmokStatus.WAIT;
        painter.erasePallet();
        painter.fill(BOARD, 1, 1);
        isBlackTurn = true;
    }

    public boolean isEnd() {
        return CommonUtil.anySame(status,
                OmokStatus.WHITE_WIN,
                OmokStatus.BLACK_WIN,
                OmokStatus.BLACK_RESIGN,
                OmokStatus.WHITE_RESIGN,
                OmokStatus.DRAW);
    }

    public enum OmokStatus {
        WAIT("착수 대기 중"),
        WHITE_WIN(OmokGame.WHITE.getEmoji() + " 승리"),
        BLACK_WIN(OmokGame.BLACK.getEmoji() + " 승리"),
        WHITE_RESIGN(OmokGame.WHITE.getEmoji() + " 기권"),
        BLACK_RESIGN(OmokGame.BLACK.getEmoji() + " 기권"),
        DRAW("무승부"),
        PREVIEW("미리보기"),
        PREV("로그 확인");

        @Getter
        private final String display;

        OmokStatus(String display) {
            this.display = display;
        }
    }

}
