package net.qsef1256.diabot.util;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Random;

@UtilityClass
public class CommonUtil {

    public int randomInt(int min, int max) {
        return new Random().nextInt(max + 1 - min) + min;
    }

    public boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    public <T> T getRandomElement(final List<T> list) {
        return list.get(new Random().nextInt(list.size()));
    }

}
