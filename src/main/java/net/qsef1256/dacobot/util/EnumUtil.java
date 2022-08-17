package net.qsef1256.dacobot.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class EnumUtil {

    /**
     * @return Enum mapped by function
     */
    @NotNull
    @UnmodifiableView
    public static <T extends Enum<T>, K> Map<K, T> toMap(Function<T, K> function, @NotNull Class<T> enumClass) {
        return Collections.unmodifiableMap(Stream.of(enumClass.getEnumConstants()).collect(Collectors.toMap(function, Function.identity())));
    }

}
