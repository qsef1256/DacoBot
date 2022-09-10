package net.qsef1256.dacobot.game.board.sudoku.model;

import com.google.common.primitives.Bytes;
import lombok.Getter;
import lombok.Setter;
import net.qsef1256.dacobot.game.board.ability.Solvable;
import net.qsef1256.dacobot.game.board.model.BoardGame;
import net.qsef1256.dacobot.game.paint.enums.Emoji;
import net.qsef1256.dacobot.game.paint.enums.NumberEmoji;
import net.qsef1256.dacobot.game.paint.model.painter.Painter;
import net.qsef1256.dacobot.util.CommonUtil;
import net.qsef1256.dacobot.util.MatrixUtil;
import net.qsef1256.dacobot.util.PrimitiveUtil;
import net.qsef1256.dacobot.util.RandomUtil;
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

    /**
     * 스도쿠 보드를 나타냅니다.
     * <p>
     * 실제 플레이는 {@link SudokuGame} 으로 진행합니다.
     *
     * @author Ankur Trisal (ankur.trisal@gmail.com), refactoring qsef1256
     * @implNote 소스 해석 실패로 private 인 메서드는 0부터 시작합니다.
     * @see <a href="https://www.geeksforgeeks.org/program-sudoku-generator/">https://www.geeksforgeeks.org/program-sudoku-generator/</a>
     * @see SudokuGame
     */
    public static class SudokuBoard implements Cloneable {

        public static final int X_START = 1;
        public static final int Y_START = 1;

        private static final int GRID_SIZE = 9;
        private static final int BLOCK_SIZE = 3;

        public static final byte EMPTY = 0;

        @Getter
        @Setter
        private byte[][] grid = new byte[GRID_SIZE][GRID_SIZE];

        public SudokuBoard(int toRemove) {
            fillDiagonal();
            fillRemaining(0, BLOCK_SIZE);
            removeDigits(toRemove);
        }

        /**
         * 해당 위치에 스도쿠 규칙을 따라서 놓을 수 있는지 체크합니다.
         *
         * @param x      col (> 0)
         * @param y      row (> 0)
         * @param number number to check
         * @return true when can place number
         */
        public boolean canSet(int x, int y, int number) {
            checkNumber(number);
            checkInBound(x, y);

            x -= X_START;
            y -= Y_START;

            // Temporary set number to 0
            byte temp = grid[y][x];
            grid[y][x] = 0;

            boolean result = checkRow(y, number) && checkCol(x, number) && checkBlock(y - y % BLOCK_SIZE, x - x % BLOCK_SIZE, number);

            grid[y][x] = temp;
            return result;
        }

        /**
         * 해당 위치에 뭔가 들어갈 수 있는지 체크합니다.
         *
         * @param x col (> 0)
         * @param y row (> 0)
         * @return true when is in bound
         */
        public boolean isInBound(int x, int y) {
            return CommonUtil.isBetween(X_START, x, GRID_SIZE) && CommonUtil.isBetween(Y_START, y, GRID_SIZE);
        }

        public byte getNumber(int x, int y) {
            return grid[y - Y_START][x - X_START];
        }

        /**
         * 숫자를 강제로 설정합니다.
         *
         * @param x      col (> 0)
         * @param y      row (> 0)
         * @param number number to set
         */
        public void setNumber(int x, int y, byte number) {
            checkNumber(number);
            checkInBound(x, y);

            grid[y - Y_START][x - X_START] = number;
        }

        /**
         * 규칙을 위반한 숫자 목록을 배열로 반환합니다.
         *
         * @return true when illegal
         */
        public boolean[][] getIllegal() {
            boolean[][] result = new boolean[GRID_SIZE][GRID_SIZE];

            for (int y = 0; y < GRID_SIZE; y++) {
                for (int x = 0; x < GRID_SIZE; x++) {
                    if (grid[y][x] == EMPTY) continue;

                    result[y][x] = !canSet(x + X_START, y + Y_START, grid[y][x]);
                }
            }
            return result;
        }

        private void checkNumber(int number) {
            if (number == EMPTY) return;
            if (!CommonUtil.isBetween(1, number, 9))
                throw new IllegalArgumentException("올바르지 않은 숫자입니다: " + number);
        }

        public void checkInBound(int x, int y) {
            if (!isInBound(x, y))
                throw new IllegalArgumentException("범위를 벗어났습니다. x: %s, y: %s".formatted(x, y));
        }

        private boolean checkBlock(int y, int x, int num) {
            for (int rowY = 0; rowY < BLOCK_SIZE; rowY++)
                for (int colX = 0; colX < BLOCK_SIZE; colX++)
                    if (grid[y + rowY][x + colX] == num) return false;

            return true;
        }

        private boolean checkRow(int y, int num) {
            for (int x = 0; x < GRID_SIZE; x++)
                if (grid[y][x] == num) return false;
            return true;
        }

        private boolean checkCol(int x, int num) {
            for (int y = 0; y < GRID_SIZE; y++)
                if (grid[y][x] == num) return false;
            return true;
        }

        private void fillDiagonal() {
            for (int i = 0; i < GRID_SIZE; i = i + BLOCK_SIZE)
                fillBlock(i, i);
        }

        private void fillBlock(int y, int x) {
            int num;
            for (int rowY = 0; rowY < BLOCK_SIZE; rowY++) {
                for (int colX = 0; colX < BLOCK_SIZE; colX++) {
                    do {
                        num = RandomUtil.randomInt(1, GRID_SIZE);
                    } while (!checkBlock(y, x, num));

                    grid[y + rowY][x + colX] = (byte) num;
                }
            }
        }

        private boolean fillRemaining(int y, int x) {
            if (x >= GRID_SIZE && y < GRID_SIZE - 1) {
                y = y + 1;
                x = 0;
            }
            if (y >= GRID_SIZE && x >= GRID_SIZE) return true;

            if (y < BLOCK_SIZE) {
                if (x < BLOCK_SIZE) x = BLOCK_SIZE;
            } else if (y < GRID_SIZE - BLOCK_SIZE) {
                if (x == (y / BLOCK_SIZE) * BLOCK_SIZE) x = x + BLOCK_SIZE;
            } else {
                if (x == GRID_SIZE - BLOCK_SIZE) {
                    y = y + 1;
                    x = 0;
                    if (y >= GRID_SIZE) return true;
                }
            }

            for (int num = 1; num <= GRID_SIZE; num++) {
                if (canSet(x + X_START, y + Y_START, num)) {
                    grid[y][x] = (byte) num;
                    if (fillRemaining(y, x + 1)) return true;

                    grid[y][x] = 0;
                }
            }
            return false;
        }

        /**
         * 스도쿠를 해결합니다.
         *
         * @return true when solved
         * @see <a href="https://www.geeksforgeeks.org/sudoku-backtracking-7/">www.geeksforgeeks.org/sudoku-backtracking-7/</a>
         */
        public boolean solve() {
            return solve(0, 0);
        }

        private boolean solve(int row, int col) {
            if (row == GRID_SIZE - 1 && col == GRID_SIZE) return true;
            if (col == GRID_SIZE) {
                row++;
                col = 0;
            }

            if (grid[row][col] != 0) return solve(row, col + 1);
            for (int num = 1; num < 10; num++) {
                if (canSet(col + X_START, row + Y_START, num)) {
                    grid[row][col] = (byte) num;

                    if (solve(row, col + 1)) return true;
                }
                grid[row][col] = 0;
            }
            return false;
        }

        private void removeDigits(int toRemove) {
            int count = toRemove;
            while (count != 0) {
                int cellId = RandomUtil.randomInt(1, GRID_SIZE * GRID_SIZE) - 1;

                int y = (cellId / GRID_SIZE);
                int x = cellId % 9;
                if (x != 0) x = x - 1;
                if (grid[y][x] != 0) {
                    count--;
                    grid[y][x] = 0;
                }
            }
        }

        public String printSudoku() {
            StringBuilder result = new StringBuilder();

            for (int y = 0; y < GRID_SIZE; y++) {
                for (int x = 0; x < GRID_SIZE; x++)
                    result.append(grid[y][x]).append(" ");
                result.append("\n");
            }
            result.append("\n");
            return result.toString();
        }

        public boolean isEnd() {
            return !new MatrixUtil<>(Byte.class).anyMatch(PrimitiveUtil.toObject(grid), (byte) 0);
        }

        public SudokuBoard copy() {
            try {
                SudokuBoard clone = (SudokuBoard) super.clone();
                MatrixUtil<Byte> util = new MatrixUtil<>(Byte.class);

                clone.grid = PrimitiveUtil.toPrimitive(util.deepCopy(PrimitiveUtil.toObject(getGrid())));

                return clone;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }

    }

}
