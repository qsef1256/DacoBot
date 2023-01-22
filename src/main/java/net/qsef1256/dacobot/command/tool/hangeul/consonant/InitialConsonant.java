package net.qsef1256.dacobot.command.tool.hangeul.consonant;

import lombok.Getter;
import net.qsef1256.dialib.util.EnumUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Getter
public enum InitialConsonant implements Consonant {

    GIYEOG("r", "ㄱ"),
    SSANG_GIYEOG("R", "ㄲ"),
    NIEUN("s", "ㄴ"),
    DIGEUD("e", "ㄷ"),
    SSANG_DIGEUD("E", "ㄸ"),
    LIEUL("f", "ㄹ"),
    MIEUM("a", "ㅁ"),
    BIEUB("q", "ㅂ"),
    SSANG_BIEUB("Q", "ㅃ"),
    SIOS("t", "ㅅ"),
    SSANG_SIEUD("T", "ㅆ"),
    IEUNG("d", "ㅇ"),
    JIEUJ("w", "ㅈ"),
    SSANG_JIEUJ("W", "ㅉ"),
    CHIEUCH("c", "ㅊ"),
    KIEUK("z", "ㅋ"),
    TIEUT("x", "ㅌ"),
    PIEUP("v", "ㅍ"),
    HIEUH("g", "ㅎ");

    private static final Map<String, InitialConsonant> fromKor =
            EnumUtil.toMap(InitialConsonant::getKor, InitialConsonant.class);
    private static final Map<String, InitialConsonant> fromEng =
            EnumUtil.toMap(InitialConsonant::getEng, InitialConsonant.class);
    @NotNull
    private final String eng;
    @NotNull
    private final String kor;

    InitialConsonant(@NotNull String eng, @NotNull String kor) {
        this.eng = eng;
        this.kor = kor;
    }

    @Nullable
    public static InitialConsonant fromKor(String kor) {
        return fromKor.get(kor);
    }

    @Nullable
    public static InitialConsonant fromEng(@NotNull String eng) {
        if (fromEng.containsKey(eng.toUpperCase())) return fromEng.get(eng);

        return fromEng.get(eng.toLowerCase());
    }

}
