package net.qsef1256.dacobot.command.tool.hangeul.consonant;

import lombok.Getter;
import net.qsef1256.dialib.util.EnumUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Getter
public enum FinalConsonant {

    NONE("", ""),
    GIYEOG("r", "ㄱ"),
    SSANG_GIYEOG("R", "ㄲ"),
    GIYEOG_SIOS("rt", "ㄳ"),
    NIEUN("s", "ㄴ"),
    NIEUN_JIEUJ("sw", "ㄵ"),
    NIEUN_HIEUH("sg", "ㄶ"),
    DIGEUD("e", "ㄷ"),
    LIEUL("f", "ㄹ"),
    LIEUL_GIYEOG("fr", "ㄺ"),
    LIEUL_MIEUM("fa", "ㄻ"),
    LIEUL_BIEUB("fq", "ㄼ"),
    LIEUL_SIOS("ft", "ㄽ"),
    LIEUL_TIEUT("fx", "ㄾ"),
    LIEUL_PIEUP("fv", "ㄿ"),
    LIEUL_HIEUH("fg", "ㅀ"),
    MIEUM("a", "ㅁ"),
    BIEUB("q", "ㅂ"),
    BIEUB_SIOS("qt", "ㅄ"),
    SIOS("t", "ㅅ"),
    SSANG_SIEUD("T", "ㅆ"),
    IEUNG("d", "ㅇ"),
    JIEUJ("w", "ㅈ"),
    CHIEUCH("c", "ㅊ"),
    KIEUK("z", "ㅋ"),
    TIEUT("x", "ㅌ"),
    PIEUP("v", "ㅍ"),
    HIEUH("g", "ㅎ");

    private static final Map<String, FinalConsonant> fromKor =
            EnumUtil.toMap(FinalConsonant::getKor, FinalConsonant.class);
    private static final Map<String, FinalConsonant> fromEng =
            EnumUtil.toMap(FinalConsonant::getEng, FinalConsonant.class);

    private final String eng;
    private final String kor;

    FinalConsonant(String eng, String kor) {
        this.eng = eng;
        this.kor = kor;
    }

    @Nullable
    public static FinalConsonant fromKor(String kor) {
        return fromKor.get(kor);
    }

    @Nullable
    public static FinalConsonant fromEng(@NotNull String eng) {
        if (fromEng.containsKey(eng.toUpperCase())) return fromEng.get(eng);

        return fromEng.get(eng.toLowerCase());
    }

}
