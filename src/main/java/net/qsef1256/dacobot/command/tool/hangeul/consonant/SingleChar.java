package net.qsef1256.dacobot.command.tool.hangeul.consonant;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@Getter
public class SingleChar<T extends Consonant> {

    private final @NotNull String eng;

    @Nullable
    private T consonant;
    private String character;

    public SingleChar(@NotNull String eng, Function<String, T> transformer) {
        this.eng = eng;

        if (eng.length() >= 2) {
            character = String.valueOf(eng.charAt(0)) + eng.charAt(1);
            consonant = transformer.apply(character);
        }

        if (eng.length() >= 1 && consonant == null) {
            character = String.valueOf(eng.charAt(0));
            consonant = transformer.apply(character);
        }

        if (consonant == null) {
            character = "";
            // TODO: for FinalConsonant, return FinalConsonant.NONE;
        }
    }

    public boolean hasConsonant() {
        return consonant != null;
    }

    public boolean hasNextConsonant(Function<String, MedialConsonant> fromEng) {
        int nextStart = Math.min(character.length(), eng.length());

        return new SingleChar<>(eng.substring(nextStart), fromEng).getConsonant() != null;
    }

    @Override
    public String toString() {
        return consonant != null ? consonant.getKor() : character;
    }

}
