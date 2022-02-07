package net.qsef1256.diabot.game.explosion.enums;

import lombok.Getter;
import net.qsef1256.diabot.enums.DiaColor;

import java.awt.*;

public enum ItemRank {

    CRUDE("저급", "조약돌", new Color(63, 63, 63)),
    COMMON("일반", "나무", new Color(255, 255, 255)),
    UNCOMMON("고급", "철괴", new Color(72, 255, 0)),
    RARE("레어", "금괴", new Color(44, 160, 255)),
    EPIC("에픽", "인첸트 북", new Color(139, 44, 255)),
    UNIQUE("유니크", "다이아", new Color(255, 44, 164)),
    LEGENDARY("전설", "네더의 별", new Color(255, 215, 12)),
    SPECIAL("특별", "???", DiaColor.MAIN_COLOR),
    TEST("테스트", "배리어", new Color(187, 255, 249));

    @Getter
    private final String title;
    @Getter
    private final String desc;
    @Getter
    private final Color color;

    ItemRank(String title, String desc, Color color) {
        this.title = title;
        this.desc = desc;
        this.color = color;
    }

}
