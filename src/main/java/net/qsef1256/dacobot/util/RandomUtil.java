package net.qsef1256.dacobot.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@UtilityClass
public class RandomUtil {

    @Getter
    private static final Random random = new Random();

    /**
     * min 과 max 사이의 정수를 랜덤하게 뽑습니다. <b>max를 포함합니다.</b>
     *
     * @param min min value
     * @param max max value
     * @return min <= value <= max
     */
    public int randomInt(int min, int max) {
        return random.nextInt(min, max + 1);
    }

    public boolean randomBool() {
        return randomInt(0, 1) == 0;
    }

    public <T> T getRandomElement(@NotNull List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    public <T> List<T> getMultiRandomElement(@NotNull List<T> list, int count) {
        List<T> original = new ArrayList<>(list);
        List<T> result = new ArrayList<>();

        if (list.isEmpty()) return List.of();
        for (int i = 0; i < Math.min(count, list.size()); i++) {
            T random = getRandomElement(original);
            original.remove(random);
            result.add(random);
        }
        return result;
    }

}
