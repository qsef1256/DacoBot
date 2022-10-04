package net.qsef1256.dacobot.game.hangman.container;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import net.qsef1256.dacobot.game.hangman.domain.HangMan;

@UtilityClass
public class HangManContainer {

    @Getter
    @Setter
    private static HangMan hangman;

}
