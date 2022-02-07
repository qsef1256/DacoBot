package net.qsef1256.diabot.game.omok.model;

import lombok.Getter;
import net.qsef1256.diabot.game.paint.enums.PixelColor;
import net.qsef1256.diabot.game.paint.model.painter.Painter;
import net.qsef1256.diabot.util.CommonUtil;
import org.jetbrains.annotations.NotNull;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.qsef1256.diabot.DiaBot.logger;

// ax = x - 1, ay = y - 1
public class OmokGame {

    public static final PixelColor BOARD = PixelColor.BROWN;
    public static final PixelColor BLACK = PixelColor.BLACK;
    public static final PixelColor WHITE = PixelColor.WHITE;
    public static final PixelColor PREVIEW = PixelColor.GREEN;

    @Getter
    private final Painter painter;
    @Getter
    private boolean isBlackTurn;
    @Getter
    private OmokStatus status;

    private int prevX, prevY, previewX, previewY;
    private PixelColor previewColor;

    public OmokGame() {
        painter = new Painter(13, 13);
        reset();
    }

    @NotNull
    private static String getNumMok(@NotNull PixelColor color, int count) {
        return String.valueOf(color.getEmoji()).repeat(count);
    }

    public String printBoard() {
        return painter.printToString();
    }

    public void prevStone() {
        previewColor = painter.getPixel(prevX, prevY);
        painter.paintPixel(PREVIEW, prevX, prevY);
        status = OmokStatus.PREV;
    }

    public void previewStone(int x, int y, PixelColor stone) {
        validate(x, y, stone);
        previewColor = painter.getPixel(x, y);
        previewX = x;
        previewY = y;
        painter.paintPixel(PREVIEW, previewX - 1, previewY - 1);
        status = OmokStatus.PREVIEW;
    }

    public void unPreview() {
        painter.paintPixel(previewColor, previewX - 1, previewY - 1);
        previewColor = null;
        status = OmokStatus.PROGRESS;
    }

    public void placeStone(int x, int y, PixelColor stone) {
        logger.info("status: " + status.getDisplay());
        validate(x, y, stone);

        if (status == OmokStatus.PREVIEW || status == OmokStatus.PREV) unPreview();
        if (status != OmokStatus.PROGRESS) throw new IllegalStateException("게임이 진행 중이지 않습니다.");

        prevX = x;
        prevY = y;
        processStone(stone, x - 1, y - 1);
        isBlackTurn = !isBlackTurn;
    }

    public void confirmStone(PixelColor stone) {
        if (status != OmokStatus.PREVIEW) throw new IllegalStateException("미리보기 중이 아닙니다.");

        placeStone(previewX, previewY, stone);
    }

    private PixelColor getStone() {
        return isBlackTurn ? OmokGame.BLACK : OmokGame.WHITE;
    }

    private void processStone(PixelColor stone, int mx, int my) {
        logger.info("OmokGame#processStone> mx: %s my: %s".formatted(mx, my));

        painter.paintPixel(stone, mx, my);
        if (checkWin(buildCheckString(mx, my, 1, 0), stone)) win(stone);
        if (checkWin(buildCheckString(mx, my, 0, 1), stone)) win(stone);
        if (checkWin(buildCheckString(mx, my, 1, 1), stone)) win(stone);
        if (checkWin(buildCheckString(mx, my, 1, -1), stone)) win(stone);
        if (checkDraw()) draw();
    }

    private void validate(int x, int y, PixelColor stone) {
        logger.info("OmokGame#validate> x: %s y: %s".formatted(x, y));
        if (!painter.isInBound(x - 1, y - 1))
            throw new IllegalArgumentException("잘못된 좌표입니다. 입력한 x: " + x + " 입력한 y: " + y);
        if (stone != BLACK && stone != WHITE)
            throw new IllegalArgumentException("잘못된 돌 입니다: " + stone);
        logger.info("Color: " + painter.getPixel(x, y));
        if (painter.getPixel(x - 1, y - 1) != BOARD)
            throw new KeyAlreadyExistsException("거기엔 이미 돌이 놓여져 있습니다!");
        if (getStone() != stone)
            throw new IllegalStateException("당신의 차례가 아닙니다. 현재 차례: " + (isBlackTurn ? "흑돌" : "백돌"));
    }

    @NotNull
    private String buildCheckString(int mx, int my, int dx, int dy) {
        int checkLength = 6;
        int topY = my - dx * checkLength;
        int topX = mx - dy * checkLength;

        PixelColor[] stones = new PixelColor[13];
        int count = 0, destY = topY, destX = topX;
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
        for (PixelColor pixelColor : stones) {
            sb.append(pixelColor != null ? pixelColor.getEmoji() : "");
        }

        String checkString = sb.toString();
        System.out.println("check: " + checkString);
        return checkString;
    }

    private boolean checkWin(@NotNull String checkString, PixelColor stone) {
        if (checkString.contains(getNumMok(BLACK, 6)) || checkString.contains(getNumMok(WHITE, 6))) return false;
        return checkString.contains(getNumMok(BLACK, 5)) && stone == BLACK || checkString.contains(getNumMok(WHITE, 5)) && stone == WHITE;
    }

    private boolean checkDraw() {
        AtomicBoolean anyEmpty = new AtomicBoolean(false);
        painter.getPixels().forEach(pixel -> {
            if (pixel.getPixelColor() == OmokGame.BOARD) anyEmpty.set(true);
        });
        return !anyEmpty.get();
    }

    private void win(PixelColor stone) {
        status = (stone == WHITE) ? OmokStatus.WHITE_WIN : OmokStatus.BLACK_WIN;
    }

    private void draw() {
        status = OmokStatus.DRAW;
    }

    public void resign(PixelColor stone) {
        status = (stone == WHITE) ? OmokStatus.WHITE_RESIGN : OmokStatus.BLACK_RESIGN;
    }

    public void reset() {
        status = OmokStatus.PROGRESS;
        painter.erasePallet();
        painter.fill(BOARD, 0, 0);
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
        PROGRESS("진행 중"),
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
