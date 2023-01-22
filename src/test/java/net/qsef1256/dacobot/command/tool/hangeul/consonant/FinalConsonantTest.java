package net.qsef1256.dacobot.command.tool.hangeul.consonant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FinalConsonantTest {

    @Test
    void fromEng() {
        assertNull(FinalConsonant.fromEng("!"));
        assertEquals(FinalConsonant.NONE, FinalConsonant.fromEng(""));
    }

}