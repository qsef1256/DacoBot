package net.qsef1256.dacobot.command.tool.hangeul;

import lombok.Getter;
import lombok.SneakyThrows;
import net.qsef1256.dacobot.command.tool.hangeul.consonant.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <a href="https://namu.wiki/w/%ED%95%9C%EC%98%81%20%EC%A0%84%ED%99%98%20%EB%8B%A8%EC%96%B4">한영 전환 단어</a>를 변환합니다.
 *
 * @see <a href="https://cfdf.tistory.com/45">reference</a>
 */
public class KorEngConverter {

    @Getter
    private static final KorEngConverter instance = new KorEngConverter();

    // TODO: finish this
    public String korToEng(@NotNull String kor) {
        return "TODO";
    }

    @SneakyThrows
    public String engToKor(@NotNull String eng) {
        StringBuilder result = new StringBuilder();

        while (!eng.isBlank()) {
            AtomicInteger removeCount = new AtomicInteger(0);

            result.append(processSingleChar(eng.substring(0, Math.min(6, eng.length())), removeCount));
            eng = StringUtils.substring(eng, removeCount.get());
        }

        return result.toString();
    }

    /**
     * Process eng string to single kor char
     *
     * <p>Algorithm:
     * <ol>
     * <li>초성으로 시작해야 함 (다른 경우에는 무시, 한 글자를 완성할 수 없음)</li>
     * <li>중성이 다음에 들어와야 함, 다음 글자를 2개 짜리 먼저, 1개 순으로 확인 (다른 경우에는 무시, 한 글자를 완성할 수 없음)</li>
     * <li> 종성이 초성으로도 사용될 수 있는 경우 다음 글자가 중성인지 확인하고 맞다면 1로 돌아감 (이때 '글고' 처럼 종성이 남아있을 수 있음)</li>
     * <li>종성은 없어도 되나, 다음 글자를 2개 짜리 먼저, 1개 순으로 확인해서 종성이 있는지 확인하고 있을 경우 글자에 포함함</li>
     * </ol>
     * </p>
     *
     * @param eng         end char to process, mutable
     * @param removeCount processed (and removed) word count, start from 0
     * @return single kor char
     */
    @NotNull
    private String processSingleChar(@NotNull String eng, AtomicInteger removeCount) {
        // 1.
        // check Final Consonant only
        SingleChar<FinalConsonant> lastConsonant = new SingleChar<>(eng, FinalConsonant::fromEng);
        if (lastConsonant.hasConsonant() && !lastConsonant.hasNextConsonant(MedialConsonant::fromEng)) {
            removeCount.addAndGet(lastConsonant.getCharacter().length());

            return lastConsonant.toString();
        }

        // check MedialConsonant only
        SingleChar<MedialConsonant> medialConsonant = new SingleChar<>(eng, MedialConsonant::fromEng);
        if (medialConsonant.hasConsonant()) {
            removeCount.addAndGet(medialConsonant.getCharacter().length());

            return medialConsonant.toString();
        }

        // start to create character
        String initialChar = String.valueOf(eng.charAt(0));
        InitialConsonant initial = InitialConsonant.fromEng(initialChar);

        if (initial == null) {
            removeCount.addAndGet(initialChar.length());
            return initialChar;
        }

        removeCount.addAndGet(initialChar.length());
        eng = StringUtils.removeStart(eng, initialChar);

        if (eng.isBlank()) return initial.getKor();

        // 2.
        SingleChar<MedialConsonant> medial = new SingleChar<>(eng, MedialConsonant::fromEng);
        if (medial.getConsonant() == null) return initial.getKor();

        removeCount.addAndGet(medial.getCharacter().length());
        eng = StringUtils.removeStart(eng, medial.getCharacter());

        // 3.
        // case of with FinalConsonant
        if (checkGhostFire(eng, 1)) {
            String lastChar = String.valueOf(eng.charAt(0));
            FinalConsonant last = FinalConsonant.fromEng(lastChar);

            if (last == null) {
                lastChar = "";
                last = FinalConsonant.NONE;
            }
            removeCount.addAndGet(lastChar.length());

            return new SingleKorChar(initial, medial.getConsonant(), last).toString();
        }

        // case of without FinalConsonant
        if (checkGhostFire(eng, 0)) {
            return new SingleKorChar(initial, medial.getConsonant(), FinalConsonant.NONE).toString();
        }

        // 4.
        SingleChar<FinalConsonant> last = new SingleChar<>(eng, FinalConsonant::fromEng);
        removeCount.addAndGet(last.getCharacter().length());

        return new SingleKorChar(initial, medial.getConsonant(), Optional
                .ofNullable(last.getConsonant())
                .orElse(FinalConsonant.NONE)).toString();
    }

    /**
     * <a href="https://namu.wiki/w/%EB%8F%84%EA%B9%A8%EB%B9%84%EB%B6%88%20%ED%98%84%EC%83%81">도깨비불 현상</a> 을 확인합니다.
     *
     * @param pointer location of start checking
     * @return if a ghost fire phenomenon is active, return true
     */
    private boolean checkGhostFire(@NotNull String eng, int pointer) {
        if (eng.length() >= 2 + pointer && InitialConsonant.fromEng(String.valueOf(eng.charAt(pointer))) != null) {
            String secondMedialChar;
            MedialConsonant secondMedial = null;

            String firstMedialChar = String.valueOf(eng.charAt(1 + pointer));

            if (eng.length() >= 3 + pointer) {
                secondMedialChar = firstMedialChar + eng.charAt(2 + pointer);
                secondMedial = MedialConsonant.fromEng(secondMedialChar);
            }

            if (secondMedial == null) {
                secondMedialChar = firstMedialChar;
                secondMedial = MedialConsonant.fromEng(secondMedialChar);
            }

            return secondMedial != null;
        }
        return false;
    }

}
