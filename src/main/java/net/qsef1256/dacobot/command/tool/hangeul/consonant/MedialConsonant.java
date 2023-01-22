package net.qsef1256.dacobot.command.tool.hangeul.consonant;

import lombok.Getter;
import net.qsef1256.dialib.util.EnumUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Getter
public enum MedialConsonant implements Consonant {

    AH("k", "ㅏ"),
    AE("o", "ㅐ"),
    YA("i", "ㅑ"),
    YAE("O", "ㅒ"),
    UH("j", "ㅓ"),
    EH("p", "ㅔ"),
    YEO("u", "ㅕ"),
    YE("P", "ㅖ"),
    OH("h", "ㅗ"),
    WA("hk", "ㅘ"),
    WAE("ho", "ㅙ"),
    OE("hl", "ㅚ"),
    YO("y", "ㅛ"),
    U("n", "ㅜ"),
    WO("nj", "ㅝ"),
    WE("np", "ㅞ"),
    WI("nl", "ㅟ"),
    YU("b", "ㅠ"),
    EU("m", "ㅡ"),
    UI("ml", "ㅢ"),
    I("l", "ㅣ");

    private static final Map<String, MedialConsonant> fromKor =
            EnumUtil.toMap(MedialConsonant::getKor, MedialConsonant.class);
    private static final Map<String, MedialConsonant> fromEng =
            EnumUtil.toMap(MedialConsonant::getEng, MedialConsonant.class);
    @NotNull
    private final String eng;
    @NotNull
    private final String kor;

    MedialConsonant(@NotNull String eng, @NotNull String kor) {
        this.eng = eng;
        this.kor = kor;
    }

    @Nullable
    public static MedialConsonant fromKor(String kor) {
        return fromKor.get(kor);
    }

    @Nullable
    public static MedialConsonant fromEng(@NotNull String eng) {
        if (fromEng.containsKey(eng.toUpperCase())) return fromEng.get(eng);

        return fromEng.get(eng.toLowerCase());
    }

}
