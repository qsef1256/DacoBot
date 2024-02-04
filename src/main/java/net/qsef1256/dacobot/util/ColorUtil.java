package net.qsef1256.dacobot.util;

import lombok.experimental.UtilityClass;
import net.qsef1256.dialib.util.RandomUtil;

import java.awt.*;
import java.util.Random;

@UtilityClass
public class ColorUtil {

    private static final Random random = RandomUtil.getRandom();

    /**
     * Get a random pastel rainbow color.
     *
     * @return random Pastel Color
     */
    public Color randomPastel() {
        float hue = random.nextFloat();
        float saturation = (random.nextInt(2000) + 1000) / 10000f;
        float luminance = 0.9f;

        return Color.getHSBColor(hue, saturation, luminance);
    }

    /**
     * Get a random rainbow color.
     *
     * @return random Rainbow Color
     */
    public Color randomRainbow() {
        float hue = random.nextFloat();
        float saturation = 0.9f;
        float luminance = 1;

        return Color.getHSBColor(hue, saturation, luminance);
    }

}
