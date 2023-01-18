package net.qsef1256.dacobot.command.tool.hangeul;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KorEngConverterTest {

    static KorEngConverter converter = KorEngConverter.getInstance();

    @Test
    void korToEng() {
        String kor = "안녕하세요!!!";

        // TODO
    }

    @Test
    void engToKor() {
        assertEquals("안녕하세요!!!", converter.engToKor("dkssudgktpdy!!!"));
        assertEquals("안녀", converter.engToKor("dkssu"));
        assertEquals("안녕", converter.engToKor("dkssud"));
        assertEquals("그어어얽", converter.engToKor("rmdjdjdjfr"));
        assertEquals("아무말", converter.engToKor("dkanakf"));
    }

}
