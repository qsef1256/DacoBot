package net.qsef1256.dacobot.command.tool;

import lombok.Getter;
import net.qsef1256.dialib.util.EnumUtil;

import java.util.Map;

@Getter
public enum Moss {

    A("._", 'a'),
    B("_...", 'b'),
    C("_._.", 'c'),
    D("_..", 'd'),
    E(".", 'e'),
    F(".._.", 'f'),
    G("__.", 'g'),
    H("....", 'h'),
    I("..", 'i'),
    J(".___", 'j'),
    K("_._", 'k'),
    L("._..", 'l'),
    M("__", 'm'),
    N("_.", 'n'),
    O("___", 'o'),
    P(".__.", 'p'),
    Q("__._", 'q'),
    R("._.", 'r'),
    S("...", 's'),
    T("_", 't'),
    U(".._", 'u'),
    V("..._", 'v'),
    W(".__", 'w'),
    X("_.._", 'x'),
    Y("_.__", 'y'),
    Z("__..", 'z'),
    ZERO("_____", '0'),
    ONE(".____", '1'),
    TWO("..___", '2'),
    THREE("...__", '3'),
    FOUR("...._", '4'),
    FIVE(".....", '5'),
    SIX("_....", '6'),
    SEVEN("__...", '7'),
    EIGHT("___..", '8'),
    NINE("____.", '9');

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
