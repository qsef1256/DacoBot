package net.qsef1256.dacobot.game.paint.model;

import net.qsef1256.dacobot.game.board.omok.model.OmokGame;
import net.qsef1256.dacobot.game.paint.enums.ColorEmoji;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class OmokTest {

    static OmokGame omok;

    @BeforeAll
    static void setUp() {
        omok = new OmokGame();
    }

    @Test
    void testOmok() {
        omok.place(6, 7, OmokGame.BLACK);

        assertDoesNotThrow(this::printBoard);
    }

    private void printBoard() {
        System.out.println(omok.printBoard());
    }

    // Simple portable omok game
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        omok = new OmokGame();
        boolean isBlackTurn = true;

        System.out.println("Simple Omok Game");
        System.out.println("type 'exit' to exit program");

        while (true) {
            System.out.println(omok.printBoard());
            System.out.print("x y: ");
            String input = sc.nextLine();
            System.out.println("input: " + input);

            if ("exit".equals(input)) break;
            try {
                String[] tokens = input.split(" ");
                ColorEmoji stone = (isBlackTurn) ? OmokGame.BLACK : OmokGame.WHITE;

                omok.place(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), stone);
                isBlackTurn = !isBlackTurn;
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }

            if (omok.isEnd()) {
                System.out.println(omok.getStatus().getDisplay());
                System.out.println(omok.printBoard());
                return;
            }
        }
    }

}
