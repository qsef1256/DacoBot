package net.qsef1256.dacobot.command.tool.hangeul;

import lombok.Getter;
import lombok.SneakyThrows;
import net.qsef1256.dacobot.command.tool.hangeul.consonant.FinalConsonant;
import net.qsef1256.dacobot.command.tool.hangeul.consonant.InitialConsonant;
import net.qsef1256.dacobot.command.tool.hangeul.consonant.MedialConsonant;
import net.qsef1256.dacobot.command.tool.hangeul.consonant.SingleKorChar;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * <a href="https://namu.wiki/w/%ED%95%9C%EC%98%81%20%EC%A0%84%ED%99%98%20%EB%8B%A8%EC%96%B4">한영 전환 단어</a>를 변환합니다.
 *
 * @see <a href="https://cfdf.tistory.com/45">reference</a>
 */
public class KorEngConverter {

    @Getter
    private static final KorEngConverter instance = new KorEngConverter();

    public String korToEng(@NotNull String kor) {
        return "TODO";
    }

    @SneakyThrows
    public String engToKor(@NotNull String eng) {
        StringBuilder result = new StringBuilder();

        while (!eng.isBlank()) {
            StringBuilder removeBuffer = new StringBuilder();

            result.append(processSingleChar(eng, removeBuffer));
            eng = StringUtils.removeStart(eng, removeBuffer.toString());
        }

        return result.toString();
    }

    // 1. 초성으로 시작해야 함 (다른 경우에는 무시, 한 글자를 완성할 수 없음)
    // 2. 중성이 다음에 들어와야 함, 다음 글자를 2개 짜리 먼저, 1개 순으로 확인 (다른 경우에는 무시, 한 글자를 완성할 수 없음)
    // 3. 종성이 초성으로도 사용될 수 있는 경우 다음 글자가 중성인지 확인하고 맞다면 1로 돌아감
    // 4. 종성은 없어도 되나, 다음 글자를 2개 짜리 먼저, 1개 순으로 확인해서 종성이 있는지 확인하고 있을 경우 글자에 포함함
    @NotNull
    private String processSingleChar(@NotNull String eng, @NotNull StringBuilder removeBuffer) {
        // 1
        String initialChar = String.valueOf(eng.charAt(0));
        removeBuffer.append(initialChar);

        InitialConsonant initial = InitialConsonant.fromEng(initialChar);

        eng = StringUtils.removeStart(eng, initialChar);
        if (initial == null || eng.isBlank()) {
            return initialChar;
        }

        // 2
        String medialChar = "";
        MedialConsonant medial = null;

        if (eng.length() >= 2) {
            medialChar = String.valueOf(eng.charAt(0)) + eng.charAt(1);
            medial = MedialConsonant.fromEng(medialChar);
        }

        if (medial == null) {
            medialChar = String.valueOf(eng.charAt(0));
            medial = MedialConsonant.fromEng(medialChar);
        }

        removeBuffer.append(medialChar);

        eng = StringUtils.removeStart(eng, medialChar);
        if (medial == null) {
            return initial.getKor() + medialChar;
        }

        if (eng.isBlank() || checkGhostFire(eng)) {
            return new SingleKorChar(initial, medial, FinalConsonant.NONE).toString();
        }

        // 4
        String lastChar = "";
        FinalConsonant last = null;

        if (eng.length() >= 2) {
            lastChar = String.valueOf(eng.charAt(0)) + eng.charAt(1);
            last = FinalConsonant.fromEng(lastChar);
        }

        if (last == null) {
            lastChar = String.valueOf(eng.charAt(0));
            last = FinalConsonant.fromEng(lastChar);
        }

        if (last == null) {
            lastChar = "";
            last = FinalConsonant.NONE;
        }

        removeBuffer.append(lastChar);

        return new SingleKorChar(initial, medial, last).toString();
    }

    /**
     * <a href="https://namu.wiki/w/%EB%8F%84%EA%B9%A8%EB%B9%84%EB%B6%88%20%ED%98%84%EC%83%81">도깨비불 현상</a> 을 확인합니다.
     */
    private boolean checkGhostFire(@NotNull String eng) {
        // 3
        if (InitialConsonant.fromEng(String.valueOf(eng.charAt(0))) != null) {
            String secondMedialChar;
            MedialConsonant secondMedial = null;

            if (eng.length() >= 3) {
                secondMedialChar = String.valueOf(eng.charAt(1)) + eng.charAt(2);
                secondMedial = MedialConsonant.fromEng(secondMedialChar);
            }

            if (eng.length() >= 2 && secondMedial == null) {
                secondMedialChar = String.valueOf(eng.charAt(1));
                secondMedial = MedialConsonant.fromEng(secondMedialChar);
            }

            return secondMedial != null;
        }
        return false;
    }

}
