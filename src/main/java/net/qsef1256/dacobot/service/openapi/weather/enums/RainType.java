package net.qsef1256.dacobot.service.openapi.weather.enums;

import lombok.Getter;
import net.qsef1256.dialib.util.EnumUtil;

import java.util.Map;

public enum RainType {

    NONE(0, "맑음", "☀️"),
    RAIN(1, "비", "\uD83C\uDF27️"),
    RAIN_SNOW(2, "비/눈", "\uD83C\uDF28️"),
    SNOW(3, "눈", "❄️"),
    SHOWER(4, "소나기", "⛈️"),
    SMALL_RAIN(5, "빗방울", "\uD83C\uDF27️"),
    SMALL_RAIN_SHOW(6, "빗방울 눈날림", "\uD83C\uDF28️"),
    SMALL_SNOW(7, "눈날림", "❄️");

    private static final Map<Double, RainType> codeMap = EnumUtil.toMap(RainType::getCode, RainType.class);

    @Getter
    private final double code;
    @Getter
    private final String display;
    @Getter
    private final String emoji;

    RainType(double code, String display, String emoji) {
        this.code = code;
        this.display = display;
        this.emoji = emoji;
    }

    public static RainType findByCode(double code) {
        return codeMap.get(code);
    }

}
