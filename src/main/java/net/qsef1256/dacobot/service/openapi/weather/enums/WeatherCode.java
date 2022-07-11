package net.qsef1256.dacobot.service.openapi.weather.enums;

import lombok.Getter;
import net.qsef1256.dacobot.service.openapi.weather.util.WeatherUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum WeatherCode {

    TEMP("T1H", "기온", "℃", 10, "\uD83C\uDF21"),
    RAIN_HOUR("RN1", "1시간 강수량", "mm", 8, "\uD83C\uDF02"),
    EW_WIND_SPEED("UUU", "동서 바람 성분", "m/s", 12, "\uD83D\uDCA8"),
    NS_WIND_SPEED("VVV", "남북 바람 성분", "m/s", 12, "\uD83D\uDCA8"),
    HUMIDITY("REH", "습도", "%", 8, "\uD83D\uDCA7"),
    RAIN_TYPE("PTY", "강수 형태", "", 4, "☂️"),
    WIND_DIRECT("VEC", "풍향", "°", 10, "\uD83D\uDCA8"),
    WIND_SPEED("WSD", "풍속", "m/s", 10, "\uD83D\uDCA8");

    private static final Map<String, WeatherCode> codeMap =
            Collections.unmodifiableMap(Stream.of(values()).collect(Collectors.toMap(WeatherCode::getCode, Function.identity())));

    @Getter
    private final String code;
    @Getter
    private final String desc;
    @Getter
    private final String unit;
    @Getter
    private final String emoji;

    @Getter
    private final int length;

    WeatherCode(String code, String desc, String unit, int dataLength, String emoji) {
        this.code = code;
        this.desc = desc;
        this.unit = unit;
        this.length = dataLength;
        this.emoji = emoji;
    }

    @Nullable
    public static WeatherCode findByCode(String code) {
        return codeMap.get(code);
    }

    @NotNull
    public String getDisplay(double value) {
        String display = value + unit;

        if (value > 900 || value < -900) display = "자료 없음";
        if (this == WeatherCode.RAIN_TYPE) display = RainType.findByCode(value).getDisplay();
        if (this == WeatherCode.WIND_DIRECT) display = display + " " + WeatherUtil.toCardinal(value);

        return display;
    }

}
