package net.qsef1256.dacobot.service.openapi.weather.enums;

import lombok.Getter;

public enum SkyType {

    SUNNY(1, "맑음", "☀️"),
    MOSTLY_CLOUDY(3, "구름많음", "☁️"),
    CLOUDY(4, "흐림", "\uD83C\uDF2B️");

    @Getter
    private final String desc;
    @Getter
    private final String emoji;

    @Getter
    private final double code;

    SkyType(double code, String desc, String emoji) {
        this.code = code;
        this.desc = desc;
        this.emoji = emoji;
    }

}
