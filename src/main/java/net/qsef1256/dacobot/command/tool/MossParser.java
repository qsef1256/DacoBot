package net.qsef1256.dacobot.command.tool;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class MossParser {

    public static @NotNull String read(@NotNull String input) {
        StringBuilder result = new StringBuilder();

        for (String mossStr : input.toLowerCase().split(" ")) {
            Moss moss = Moss.getFromMoss(mossStr);
            if (moss == null)
                throw new IllegalArgumentException("%s is invalid character".formatted(mossStr));

            String toChar = String.valueOf(moss.getCharacter());
            result.append(toChar);
        }

        return result.toString().trim();
    }

    public static @NotNull String write(@NotNull String input) {
        StringBuilder result = new StringBuilder();

        for (char character : input.toLowerCase().toCharArray()) {
            if (character == ' ') continue;

            Moss moss = Moss.getFromChar(character);
            if (moss == null)
                throw new IllegalArgumentException("%s is invalid character".formatted(character));

            String toMoss = moss.getMossValue();
            result.append(toMoss);
            result.append(" ");
        }

        return result.toString().trim();
    }

}
