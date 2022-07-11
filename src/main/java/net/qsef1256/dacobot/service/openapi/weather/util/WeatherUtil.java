package net.qsef1256.dacobot.service.openapi.weather.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WeatherUtil {

    /**
     * 0 ~ 360 의 풍향 값을 16 방위로 반환합니다.
     *
     * @param value Wind direction(0 ~ 360)
     * @return 16 Cardinal Direction
     * @throws IllegalArgumentException Wind direction is not in 0 ~ 360
     */
    public static String toCardinal(double value) {
        if (0 > value || value > 360)
            throw new IllegalArgumentException("풍향은 0 ~ 360도 까지만 가능합니다. 입력된 값: " + value);

        int direction = (int) Math.floor((value + 22.5 * 0.5) / 22.5);
        return switch (direction) {
            case 0, 16 -> "N";
            case 1 -> "NNE";
            case 2 -> "NE";
            case 3 -> "ENE";
            case 4 -> "E";
            case 5 -> "ESE";
            case 6 -> "SE";
            case 7 -> "SSE";
            case 8 -> "S";
            case 9 -> "SSW";
            case 10 -> "SW";
            case 11 -> "WSW";
            case 12 -> "W";
            case 13 -> "WNW";
            case 14 -> "NW";
            case 15 -> "NNW";
            default -> null;
        };
    }

}
