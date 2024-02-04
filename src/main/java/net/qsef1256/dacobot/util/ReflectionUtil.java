package net.qsef1256.dacobot.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;

@UtilityClass
public class ReflectionUtil {

    /**
     * 클래스가 Concrete 한지 확인합니다. <b>멤버 클래스와 지역 클래스는 true 로 취급합니다.</b>
     *
     * @param clazz class
     * @return is concrete class?
     * @see #isPlain(Class)
     * @see #isNested(Class)
     */
    public boolean isConcrete(@NotNull Class<?> clazz) {
        return !(clazz.isInterface()
                || clazz.isArray()
                || clazz.isPrimitive()
                || Modifier.isAbstract(clazz.getModifiers()));
    }

    /**
     * 클래스가 일반적인 클래스인지 확인합니다.
     * <p>
     * Concrete, Local, Member 여부를 확인합니다.
     * </p>
     *
     * @param clazz class
     * @return is normal class?
     * @see #isConcrete(Class)
     * @see #isNested(Class)
     */
    public boolean isPlain(@NotNull Class<?> clazz) {
        return isConcrete(clazz) && !isNested(clazz);
    }

    /**
     * 클래스가 지역 또는 멤버 클래스인지 확인합니다.
     *
     * @param clazz class
     * @return is nested class
     * @see #isConcrete(Class)
     * @see #isNested(Class)
     */
    public static boolean isNested(@NotNull Class<?> clazz) {
        return clazz.isLocalClass() || clazz.isMemberClass();
    }

}
