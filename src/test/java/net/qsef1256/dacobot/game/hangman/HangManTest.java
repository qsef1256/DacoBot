package net.qsef1256.dacobot.game.hangman;

import net.qsef1256.dacobot.game.hangman.domain.HangMan;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HangManTest {

    @Test
    void inputWord() {
        HangMan hangman = new HangMan("world");

        assertTrue(hangman.inputWord('w'));
        assertFalse(hangman.inputWord("a"));
    }

    @Test
    void getRemainLife() {
        HangMan hangman = new HangMan("world");

        hangman.inputWord('w');
        assertEquals(7, hangman.getRemainLife());

        hangman.inputWord("a");
        assertEquals(6, hangman.getRemainLife());

        hangman.inputWord("b");
        hangman.inputWord("c");
        hangman.inputWord("e");
        hangman.inputWord("f");
        hangman.inputWord("g");
        hangman.inputWord("h");
        assertEquals(0, hangman.getRemainLife());
    }

    @Test
    void getNowWord() {
        HangMan hangman = new HangMan("world");

        hangman.inputWord("w");
        hangman.inputWord("o");

        assertEquals("wo???", hangman.getDisplayWord());
    }

    @Test
    void isInputted() {
        HangMan hangman = new HangMan("world");

        hangman.inputWord("a");
        hangman.inputWord("w");

        assertTrue(hangman.isInputted('a'));
        assertTrue(hangman.isInputted('w'));
        assertFalse(hangman.isInputted('r'));
    }
    
}
