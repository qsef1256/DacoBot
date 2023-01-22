package net.qsef1256.dacobot.command.tool.hangeul.consonant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class SingleCharTest {

    @Test
    void hasConsonant() {
        assertFalse(new SingleChar<>("!", FinalConsonant::fromEng).hasConsonant());
    }
    
}