package net.qsef1256.diabot.game.paint.model;

import net.qsef1256.diabot.game.omok.model.OmokGame;
import net.qsef1256.diabot.game.paint.enums.PixelColor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

public class OmokTest {

    static OmokGame omok;

    @BeforeAll
    static void setUp() {
        omok = new OmokGame();
    }

    @Test
    public void testOmok() {
        omok.placeStone(6, 7, OmokGame.BLACK);

        printBoard();
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
                PixelColor stone = (isBlackTurn) ? OmokGame.BLACK : OmokGame.WHITE;

                omok.placeStone(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), stone);
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
