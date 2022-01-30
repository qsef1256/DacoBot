package net.qsef1256.diabot.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class CommonUtil {

    public int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public void swap(int[] toSwap, int a, int b) {
        int temp = toSwap[a];
        toSwap[a] = toSwap[b];
        toSwap[b] = temp;
    }

    public boolean isBetween(int x, int min, int max) {
        if (min > max) throw new IllegalArgumentException("min 이 max 보다 큽니다. min: %s, max: %s".formatted(min,max));
        return min <= x && x <= max;
    }

    public int toBetween(int x, int min, int max) {
        if (min > max) throw new IllegalArgumentException("min 이 max 보다 큽니다. min: %s, max: %s".formatted(min,max));
        if (x < min) x = min;
        else if (x > max) x = max;
        return x;
    }

    public int getDiff(int x, int y) {
        if (x > y) return x - y;
        else if (x < y) return y - x;
        return 0;
    }

    public <T> T getRandomElement(final List<T> list) {
        return list.get(new Random().nextInt(list.size()));
    }

}
