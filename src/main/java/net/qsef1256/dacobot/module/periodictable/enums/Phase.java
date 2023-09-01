package net.qsef1256.dacobot.module.periodictable.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.qsef1256.dialib.util.EnumUtil;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum Phase {

    SOLID("고체"),
    LIQUID("액체"),
    GAS("기체"),
    PLASMA("플라스마");

    private static final Map<String, Phase> BY_NAME = EnumUtil.toMap(Phase::getName, Phase.class);

    private final String name;

    public static Phase getByName(String name) {
        return BY_NAME.get(name);
    }

}
