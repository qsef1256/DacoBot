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
        assertEquals("ㅇ", converter.engToKor("d"));
        assertEquals("아", converter.engToKor("dk"));
        assertEquals("안", converter.engToKor("dks"));
        assertEquals("안ㄴ", converter.engToKor("dkss"));
        assertEquals("안녀", converter.engToKor("dkssu"));
        assertEquals("안녕", converter.engToKor("dkssud"));
        assertEquals("안녕하세요!!!", converter.engToKor("dkssudgktpdy!!!"));
        assertEquals("ㅁㄴㅇㄹ", converter.engToKor("asdf"));
        assertEquals("그어어얽", converter.engToKor("rmdjdjdjfr"));
        assertEquals("아무말", converter.engToKor("dkanakf"));
        assertEquals("추천이 왜 필요한 거죠", converter.engToKor("cncjsdl dho vlfdygks rjwy"));
        assertEquals("귀차니즘의 폐해", converter.engToKor("rnlckslwmadml vPgo"));
        assertEquals("궯둟쉜뤩", converter.engToKor("rnpfqenfgtnpftfnpfr"));
    }

    @Test
    void engToKorSpecial() {
        assertEquals("!", converter.engToKor("!"));
        assertEquals("ㅓ퍼ㅓㅓㄻㅁㄹ", converter.engToKor("jvjjjfaaf"));
        assertEquals("ㄻㄻㄻㄻ", converter.engToKor("fafafafa"));
        assertEquals("들고", converter.engToKor("emfrh"));
        assertEquals("글글글", converter.engToKor("rmfrmfrmf"));
        assertEquals("들긔마", converter.engToKor("emfrmlak"));
        assertEquals("닭과 꿩이 들고 있는 잔디깎이의 희망",
                converter.engToKor("ekfrrhk Rnjddl emfrh dlTsms wkselRkRdldml gmlakd"));
        assertEquals("책상의자다리짚고돌기", converter.engToKor("cortkddmlwkekflwlvrhehfrl"));
        assertEquals("마법사 돌쇠 씨의 사과", converter.engToKor("akqjqtk ehfthl Tldml tkrhk"));
        assertEquals("샘플은 많으면 많을수록 좋다 다다익선이니",
                converter.engToKor("toavmfdms aksgdmaus aksgdmftnfhr whgek ekekdlrtjsdlsl"));
        assertEquals("뉴모노울트라마이크로스코픽실리코볼케이노코니오시스",
                converter.engToKor("sbahshdnfxmfkakdlzmfhtmzhvlrtlfflzhqhfzpdlshzhsldhtltm"));
        assertEquals("깎깎", converter.engToKor("RKRRKR"));
        assertEquals("뒤틀린얀", converter.engToKor("enlxmfflsdis"));
        assertEquals("붪궖부ㅝㄽ괈", converter.engToKor("qnjfvrnjfvqnnjftrhkft"));
    }

}
