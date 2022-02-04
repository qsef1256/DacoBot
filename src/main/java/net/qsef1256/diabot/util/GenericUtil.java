package net.qsef1256.diabot.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@UtilityClass
public class GenericUtil {

    /**
     * 주어진 객체가 ArrayList 이면 ArrayList&lt;?&gt; 를 반환합니다.
     *
     * @param toCheck Object to check ArrayList
     * @return empty or transformed ArrayList&lt;?&gt;
     */
    @Contract(pure = true)
    @NotNull
    public ArrayList<?> getArrayList(Object toCheck) {
        ArrayList<?> list;
        if (toCheck instanceof ArrayList<?>) {
            list = (ArrayList<?>) toCheck;
        } else {
            list = new ArrayList<>();
        }
        return list;
    }

    @Contract(value = "_, null -> false", pure = true)
    public static boolean instanceOf(@NotNull Class<?> clazz, Class<?> targetClass) {
        return clazz.isInstance(targetClass);
    }

}
