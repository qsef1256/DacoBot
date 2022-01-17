package net.qsef1256.diabot.util;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Random;

@UtilityClass
public class CommonUtil {

    public int randomInt(int min, int max) {
        return (int) (Math.random() * max) + min;
    }

    public <T> T getRandomElement(List<T> list)
    {
        return list.get(new Random().nextInt(list.size()));
    }

}
