package net.qsef1256.dacobot.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@UtilityClass
public class CommonUtil {

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

    public <T> void swap(T @NotNull [] toSwap, int a, int b) {
        T temp = toSwap[a];
        toSwap[a] = toSwap[b];
        toSwap[b] = temp;
    }

    public boolean isBetween(int min, int val, int max) {
        if (min > max) throw new IllegalArgumentException("min 이 max 보다 큽니다. min: %s, max: %s".formatted(min, max));
        return min <= val && val <= max;
    }

    public int toBetween(int min, int val, int max) {
        if (min > max) throw new IllegalArgumentException("min 이 max 보다 큽니다. min: %s, max: %s".formatted(min, max));
        if (val < min) val = min;
        else if (val > max) val = max;
        return val;
    }

    public boolean randomBool() {
        return randomInt(0, 1) == 0;
    }

    public int getDiff(int a, int b) {
        if (a > b) return a - b;
        else if (a < b) return b - a;
        return 0;
    }

    public <T> T getRandomElement(@NotNull List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    @SafeVarargs
    public <T> boolean allSame(T... parameter) {
        HashSet<T> checkSet = new HashSet<>();

        Collections.addAll(checkSet, parameter);
        return checkSet.size() == 1;
    }

    @SafeVarargs
    public <T> boolean anySame(T... parameter) {
        HashSet<T> checkSet = new HashSet<>();

        Collections.addAll(checkSet, parameter);
        return checkSet.size() != parameter.length;
    }

    @SafeVarargs
    public <T> boolean anyNull(T... parameter) {
        return Arrays.stream(parameter).anyMatch(Objects::isNull);
    }

    @SafeVarargs
    public <T> boolean anyContains(T target, T... parameter) {
        HashSet<T> checkSet = new HashSet<>();

        Collections.addAll(checkSet, parameter);
        return checkSet.contains(target);
    }

    @SafeVarargs
    public <T> boolean allContains(T target, T @NotNull ... parameter) {
        return parameter[0] == target && allSame(parameter);
    }

    public <T> boolean linearIn(T[] outer, T[] inner) {
        return new HashSet<>(Arrays.asList(outer)).containsAll(Arrays.asList(inner));
    }

}
