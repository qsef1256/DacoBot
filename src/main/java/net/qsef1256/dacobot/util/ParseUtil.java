package net.qsef1256.dacobot.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ResultOfMethodCallIgnored")
@UtilityClass
public class ParseUtil {

    public <T> boolean canInteger(@NotNull T value) {
        if (value instanceof Integer) return true;

        return noException(() -> Integer.parseInt(value.toString()));
    }

    public <T> boolean canFloat(T value) {
        if (value instanceof Float) return true;

        return noException(() -> Float.parseFloat(value.toString()));
    }

    public <T> boolean canDouble(T value) {
        if (value instanceof Double) return true;

        return noException(() -> Double.parseDouble(value.toString()));
    }

    public <T> boolean canByte(T value) {
        if (value instanceof Byte) return true;
        return noException(() -> Byte.parseByte(value.toString()));
    }

    public <T> boolean canLong(T value) {
        if (value instanceof Long) return true;
        return noException(() -> Long.parseLong(value.toString()));
    }

    public <T> boolean canShort(T value) {
        if (value instanceof Short) return true;
        return noException(() -> Short.parseShort(value.toString()));
    }

    public <T> boolean canChar(T value) {
        if (value instanceof Character) return true;

        return value.toString().length() == 1;
    }

    public <T> boolean canString(T value) {
        return value instanceof String;
    }

    /**
     * 최소한의 크기로 변환을 시도합니다.
     *
     * @param input input
     * @param <T>   type of return value
     * @return parsed value
     * @throws ClassCastException when fail parse
     */
    @SuppressWarnings("unchecked")
    public <T> T parse(String input) {
        if (ParseUtil.canByte(input)) return (T) Byte.valueOf(input);
        if (ParseUtil.canShort(input)) return (T) Short.valueOf(input);
        if (ParseUtil.canInteger(input)) return (T) Integer.valueOf(input);
        if (ParseUtil.canLong(input)) return (T) Long.valueOf(input);
        if (ParseUtil.canFloat(input)) return (T) Float.valueOf(input);
        if (ParseUtil.canDouble(input)) return (T) Double.valueOf(input);
        if (ParseUtil.canChar(input)) return (T) Character.valueOf(input.charAt(0));
        return (T) input;
    }

    public boolean noException(Runnable runnable) {
        try {
            runnable.run();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
