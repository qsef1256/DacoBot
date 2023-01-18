package net.qsef1256.dacobot.command.tool.hangeul.consonant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SingleKorCharTest {

    @Test
    void testToString() {
        assertEquals("갉", new SingleKorChar(InitialConsonant.GIYEOG, MedialConsonant.AH, FinalConsonant.LIEUL_GIYEOG).toString());
        assertEquals("뷁", new SingleKorChar(InitialConsonant.BIEUB, MedialConsonant.WE, FinalConsonant.LIEUL_GIYEOG).toString());
        assertEquals("붸", new SingleKorChar(InitialConsonant.BIEUB, MedialConsonant.WE, FinalConsonant.NONE).toString());
    }

}
