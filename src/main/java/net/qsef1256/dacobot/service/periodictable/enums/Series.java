package net.qsef1256.dacobot.service.periodictable.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.qsef1256.dacobot.util.EnumUtil;

import java.awt.*;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum Series {

    ALKALI_METAL("알칼리 금속", "1족 원소", new Color(255, 102, 102)),
    ALKALI_EARTH_METAL("알칼리 토금속", "2족 원소", new Color(255, 222, 173)),
    LANTHANIDE("란타넘족", "원자번호 57~71", new Color(255, 191, 255)),
    ACTINIDES("악티늄족", "원자번호 89~103", new Color(255, 153, 204)),
    TRANSITION_METAL("전이 금속", "3족~12족 원소", new Color(255, 192, 192)),
    POST_TRANSITION_METAL("전이후 금속", "", new Color(204, 204, 204)),
    METALLOID("준금속", "금속과 비금속 중간", new Color(204, 204, 153)),
    NON_METAL("비금속", "금속이나 준금속이 아닌 원소", new Color(160, 255, 160)),
    HALOGEN("할로젠", "17족 원소", new Color(255, 255, 153)),
    NOBLE_GAS("비활성 기체", "18족 원소", new Color(192, 255, 255));

    private static final Map<String, Series> BY_NAME = EnumUtil.toMap(Series::getName, Series.class);

    private final String name;
    private final String desc;
    private final Color color;

    public static Series getByName(String name) {
        return BY_NAME.get(name);
    }

}
