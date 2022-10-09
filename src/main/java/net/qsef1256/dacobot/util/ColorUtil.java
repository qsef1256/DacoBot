package net.qsef1256.dacobot.util;

import lombok.experimental.UtilityClass;
import net.qsef1256.dialib.util.RandomUtil;

import java.awt.*;
import java.util.Random;

@UtilityClass
public class ColorUtil {

    private static final Random random = RandomUtil.getRandom();

    /**
     * 랜덤한 파스텔 컬러를 얻습니다.
     *
     * @return random Pastel Color
     */
    public Color randomPastel() {
        final float hue = random.nextFloat();
        final float saturation = (random.nextInt(2000) + 1000) / 10000f;
        final float luminance = 0.9f;
        return Color.getHSBColor(hue, saturation, luminance);
    }

    /**
     * 랜덤한 무지개 색을 얻습니다.
     *
     * @return random Rainbow Color
     */
    public Color randomRainbow() {
        final float hue = random.nextFloat();
        final float saturation = 0.9f;
        final float luminance = 1;
        return Color.getHSBColor(hue, saturation, luminance);
    }

}
