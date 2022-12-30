package net.qsef1256.dacobot.command.tool.moss;

import lombok.Getter;
import net.qsef1256.dialib.util.EnumUtil;

import java.util.Map;

@Getter
public enum Moss {

    A(".-", 'a'),
    B("-...", 'b'),
    C("-.-.", 'c'),
    D("-..", 'd'),
    E(".", 'e'),
    F("..-.", 'f'),
    G("--.", 'g'),
    H("....", 'h'),
    I("..", 'i'),
    J(".---", 'j'),
    K("-.-", 'k'),
    L(".-..", 'l'),
    M("--", 'm'),
    N("-.", 'n'),
    O("---", 'o'),
    P(".--.", 'p'),
    Q("--.-", 'q'),
    R(".-.", 'r'),
    S("...", 's'),
    T("-", 't'),
    U("..-", 'u'),
    V("...-", 'v'),
    W(".--", 'w'),
    X("-..-", 'x'),
    Y("-.--", 'y'),
    Z("--..", 'z'),
    ZERO("-----", '0'),
    ONE(".----", '1'),
    TWO("..---", '2'),
    THREE("...--", '3'),
    FOUR("....-", '4'),
    FIVE(".....", '5'),
    SIX("-....", '6'),
    SEVEN("--...", '7'),
    EIGHT("---..", '8'),
    NINE("----.", '9');

    private final String mossValue;
    private final char character;

    private static final Map<Character, Moss> charMap = EnumUtil.toMap(Moss::getCharacter, Moss.class);
    private static final Map<String, Moss> mossMap = EnumUtil.toMap(Moss::getMossValue, Moss.class);

    Moss(String mossValue, char character) {
        this.mossValue = mossValue;
        this.character = character;
    }

    public static Moss getFromMoss(String moss) {
        return mossMap.get(moss);
    }

    public static Moss getFromChar(char character) {
        return charMap.get(character);
    }

}
