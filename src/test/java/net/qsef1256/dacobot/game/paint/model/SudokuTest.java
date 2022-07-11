package net.qsef1256.dacobot.game.paint.model;

import net.qsef1256.dacobot.game.board.sudoku.model.SudokuGame;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class SudokuTest {

    @Test
    void testSudoku() {
        assertDoesNotThrow(() -> {
            SudokuGame sudoku = new SudokuGame();
            System.out.println(sudoku);
        });
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        SudokuGame sudoku = new SudokuGame();
        System.out.println("Simple Sudoku Game");
        System.out.println("type 'exit' to exit program");
        System.out.println("type 'solve' to resolve game");

        while (true) {
            System.out.println(sudoku.printBoard());
            System.out.print("x, y, number: ");
            String input = sc.nextLine();
            System.out.println("input: " + input);

            if ("exit".equals(input)) break;
            if ("solve".equals(input)) {
                sudoku.solve();
                System.out.println(sudoku.printBoard());

                return;
            }
            try {
                String[] tokens = input.split(" ");
                byte number = Byte.parseByte(tokens[2]);

                sudoku.place(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), number);
            } catch (RuntimeException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }

            if (sudoku.isEnd()) {
                System.out.println("Game End");
                System.out.println(sudoku.printBoard());
                return;
            }
        }
    }

    @Test
    void canSet() {
        SudokuGame.SudokuBoard sudoku = new SudokuGame.SudokuBoard(25); // TODO: private

        sudoku.setGrid(new byte[][]{
                {1, 2, 3, 0, 0, 0, 0, 0, 0},
                {4, 5, 6, 7, 0, 9, 1, 2, 3},
                {7, 0, 9, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 0, 0, 0, 0, 0, 0},
                {0, 0, 4, 0, 0, 0, 0, 0, 0},
                {0, 0, 5, 0, 0, 0, 0, 0, 0},
                {0, 0, 7, 0, 0, 0, 0, 0, 0},
        });

        // Box
        assertTrue(sudoku.canSet(2, 3, 8));
        for (int i = 0; i < 9; i++) {
            if (i == 8) continue;
            assertFalse(sudoku.canSet(2, 3, i));
        }

        // Row
        assertTrue(sudoku.canSet(5, 2, 8));
        for (int i = 0; i < 9; i++) {
            if (i == 8) continue;
            assertFalse(sudoku.canSet(5, 2, i));
        }

        // Col
        assertTrue(sudoku.canSet(3, 5, 8));
        for (int i = 0; i < 9; i++) {
            if (i == 8) continue;
            assertFalse(sudoku.canSet(3, 5, i));
        }
    }

}
