package net.qsef1256.dacobot.game.hangman.container;

import lombok.Getter;
import lombok.Setter;
import net.qsef1256.dacobot.game.hangman.domain.HangMan;

public class HangManContainer {

    @Getter
    private static final HangManContainer instance = new HangManContainer();

    @Getter
    @Setter
    private HangMan hangman;

}
