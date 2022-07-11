package net.qsef1256.dacobot.util;

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

    /**
     * 클래스의 인스턴스인지를 체크합니다.
     *
     * @param clazz       class
     * @param targetClass target class
     * @return true if class is instanceOf target
     * @see #typeOf(Class, Class)
     */
    @Contract(value = "_, null -> false", pure = true)
    public static boolean instanceOf(@NotNull Class<?> clazz, Class<?> targetClass) {
        return clazz.isInstance(targetClass);
    }

    /**
     * 타입 이름을 비교합니다.
     *
     * @param clazz       class
     * @param targetClass target class
     * @return true if type name is same
     * @see #instanceOf(Class, Class)
     */
    public static boolean typeOf(@NotNull Class<?> clazz, @NotNull Class<?> targetClass) {
        return clazz.getTypeName().equals(targetClass.getTypeName());
    }

    /**
     * 서브 클래스인지 체크합니다.
     *
     * @param clazz       class
     * @param targetClass target class
     * @return true if class is subclass of target
     */
    public static boolean isSubClassOnly(Object clazz, Class<?> targetClass) {
        return clazz != null && targetClass.isAssignableFrom((Class<?>) clazz) && clazz.getClass() != targetClass;
    }

}
