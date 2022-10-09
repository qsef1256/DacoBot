package net.qsef1256.dacobot.game.board.sudoku.model;

import com.google.common.primitives.Bytes;
import lombok.Getter;
import net.qsef1256.dacobot.game.board.ability.Solvable;
import net.qsef1256.dacobot.game.board.model.BoardGame;
import net.qsef1256.dacobot.game.board.sudoku.model.board.SudokuBoard;
import net.qsef1256.dacobot.game.paint.enums.Emoji;
import net.qsef1256.dacobot.game.paint.enums.NumberEmoji;
import net.qsef1256.dacobot.game.paint.model.painter.Painter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.qsef1256.dacobot.game.paint.model.painter.Painter.X_START;
import static net.qsef1256.dacobot.game.paint.model.painter.Painter.Y_START;

/**
 * @see SudokuInterface
 */
public class SudokuGame implements BoardGame<Byte>, Solvable {

    private final Painter painter = new Painter(9, 9);
    @Getter
    private final SudokuBoard board; // 초기 숫자
    @Getter
    private final SudokuBoard userBoard; // 현재 상태

    private static final Emoji EMPTY = NumberEmoji.EMPTY;
    private static final byte EMPTY_NUMBER = SudokuBoard.EMPTY;

    public SudokuGame() {
        this(35);
    }

    public SudokuGame(int toRemove) {
        board = new SudokuBoard(toRemove);
        userBoard = board.copy();

        initPainter(painter, board.getGrid());
    }

    @Override
    public void place(int x, int y, Byte number) {
        if (number == EMPTY_NUMBER) {
            clear(x, y);
            return;
        }

        checkLocation(x, y, number);
        userBoard.setNumber(x, y, number);
        painter.paintPixel(getBlackNumber(number), x, y);
        updatePainter();
    }

    private void clear(int x, int y) {
        board.checkInBound(x, y);

        painter.paintPixel(EMPTY, x, y);
        userBoard.setNumber(x, y, (byte) 0);
    }

    private void updatePainter() {
        boolean[][] illegal = userBoard.getIllegal();

        for (int y = 0; y < illegal.length; y++) {
            for (int x = 0; x < illegal[0].length; x++) {
                if (illegal[y][x]) {
                    NumberEmoji redNumber = getRedNumber(userBoard.getGrid()[y][x]);

                    painter.paintPixel(redNumber, x + X_START, y + Y_START);
                }
            }
        }
    }

    private void checkLocation(int x, int y, byte number) {
        if (!board.canSet(x, y, number))
            throw new IllegalArgumentException("올바르지 않은 위치입니다, x: %s y: %s number: %s".formatted(x, y, number));
    }

    public boolean isEnd() {
        return board.isEnd();
    }

    @Override
    public boolean solve() {
        boolean solved = board.solve();
        if (solved) initPainter(painter, board.getGrid());

        return solved;
    }

    private static void initPainter(@NotNull Painter painter, byte[][] sudokuArray) {
        List<Byte> byteList = Bytes.asList(Bytes.concat(sudokuArray));
        List<Emoji> numberList = new ArrayList<>();

        for (byte number : byteList) {
            if (number == 0) numberList.add(EMPTY);
            else numberList.add(getBlueNumber(number));
        }

        painter.paintAll(numberList);
    }

    @Nullable
    private static NumberEmoji getRedNumber(byte number) {
        return NumberEmoji.findById("red" + number);
    }

    @Nullable
    private static NumberEmoji getBlueNumber(byte number) {
        return NumberEmoji.findById(String.valueOf(number));
    }

    @Nullable
    private static NumberEmoji getBlackNumber(byte number) {
        return NumberEmoji.findById("black" + number);
    }

    @Override
    public String printBoard() {
        return painter.printPallet();
    }

    @Override
    public String toString() {
        return super.toString() + "[isEnd= " + isEnd() + ", board=\n" + printBoard() + "]";
    }

}
