package net.qsef1256.dacobot.command.tool.hangeul.consonant;

import org.jetbrains.annotations.NotNull;

public class SingleKorChar {

    private final InitialConsonant initial;
    private final MedialConsonant medial;
    private final FinalConsonant last;

    public SingleKorChar(@NotNull InitialConsonant initial,
                         @NotNull MedialConsonant medial,
                         @NotNull FinalConsonant last) {
        this.initial = initial;
        this.medial = medial;
        this.last = last;
    }

    @Override
    public String toString() {
        return Character.toString((char) (initial.ordinal() * 21 * 28)
                + (medial.ordinal() * 28)
                + last.ordinal() + 0xAC00);
    }

}
