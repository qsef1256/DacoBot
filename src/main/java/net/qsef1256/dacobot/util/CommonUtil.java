package net.qsef1256.dacobot.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

@UtilityClass
public class CommonUtil {

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

    public int getDiff(int a, int b) {
        if (a > b) return a - b;
        else if (a < b) return b - a;
        return 0;
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
