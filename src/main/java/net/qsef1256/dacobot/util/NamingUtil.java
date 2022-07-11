package net.qsef1256.dacobot.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class NamingUtil {

    @NotNull
    public static String toSnakeCase(@NotNull String input) {
        final String regex = "([a-z])([A-Z])";
        final String replacement = "$1_$2";
        return input.replaceAll(regex, replacement).toLowerCase();
    }

    @NotNull
    public static String toDotCase(@NotNull String input) {
        final String regex = "([a-z])([A-Z])";
        final String replacement = "$1.$2";
        return input.replaceAll(regex, replacement).toLowerCase();
    }

}
