package net.qsef1256.dacobot.game.hangman.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import net.qsef1256.dacobot.util.CommonUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class HangMan {

    @Getter
    private final String originWord;
    @Getter
    private int remainLife = 7;
    @Getter
    private final boolean[] charMatches;
    private final Set<Character> inputtedChar = new HashSet<>();

    public HangMan(@NotBlank @NotNull String word) {
        if (!word.chars().allMatch(value -> isAlphabet(String.valueOf(value))))
            throw new IllegalArgumentException("Parameter word is not letter: " + word);

        this.originWord = word.toLowerCase();
        this.charMatches = new boolean[word.length()];
    }

    public HangMan(@NotBlank @NotNull String word, @Positive int remainLife) {
        this(word);
        this.remainLife = remainLife;
    }

    @Contract(pure = true)
    private static boolean isAlphabet(@NotNull String value) {
        return value.matches("[a-zA-Z]");
    }

    /**
     * input character for Hangman game
     *
     * @param input input character
     * @return is Right character?
     * @throws IllegalArgumentException when input is invalid
     * @throws IllegalStateException    when Hangman game is end
     */
    public boolean inputWord(char input) {
        if (!isAlphabet(String.valueOf(input)))
            throw new IllegalArgumentException("Input word is not letter: %s".formatted(input));
        if (isEnd())
            throw new IllegalStateException("Hangman game already ended!");
        if (isInputted(input))
            throw new IllegalArgumentException("This character already inputted: " + input);

        char character = Character.toLowerCase(input);
        boolean isContain = false;
        int index = 0;
        for (char toCompare : originWord.toCharArray()) {
            if (character == toCompare) {
                isContain = true;
                charMatches[index] = true;
            }
            index++;
        }

        inputtedChar.add(input);
        if (!isContain) removeLife();
        return isContain;
    }

    /**
     * @see #inputWord(char)
     */
    public boolean inputWord(@NotNull String input) {
        if (input.length() != 1)
            throw new IllegalArgumentException("input string must be character (1 length)");

        return inputWord(input.toCharArray()[0]);
    }

    public String getDisplayWord() {
        return getDisplayWord('?');
    }

    public String getDisplayWord(char unknownReplacement) {
        StringBuilder nowWord = new StringBuilder();

        int index = 0;
        for (boolean isMatch : charMatches) {
            if (isMatch)
                nowWord.append(originWord.charAt(index));
            else
                nowWord.append(unknownReplacement);
            index++;
        }
        return nowWord.toString();
    }

    public boolean isInputted(char input) {
        return inputtedChar.contains(input);
    }

    private void removeLife() {
        remainLife--;
    }

    public boolean isFail() {
        return remainLife <= 0;
    }

    public boolean isSuccess() {
        return CommonUtil.allContains(charMatches, true);
    }

    public boolean isEnd() {
        return isFail() || isSuccess();
    }

}
