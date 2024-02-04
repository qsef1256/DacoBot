package net.qsef1256.dacobot.util;

import lombok.experimental.UtilityClass;
import net.qsef1256.dialib.util.TryUtil;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

@UtilityClass
public class TimeUtil {

    /**
     * parsing time with hms format
     *
     * @param hms time string like "10h 15m 30s"
     * @return duration, default {@code Duration.ZERO}
     */
    public Duration parseHms(@NotNull String hms) {
        Duration duration = Duration.ZERO;
        String[] hmsSplit = hms.split(" ");

        for (String hmsPart : hmsSplit) {
            String suffix = hmsPart.substring(hmsPart.length() - 1).toLowerCase();

            switch (suffix) {
                case "s" -> duration = duration.plusSeconds(getTimeToAdd(hmsPart));
                case "m" -> duration = duration.plusMinutes(getTimeToAdd(hmsPart));
                case "h" -> duration = duration.plusHours(getTimeToAdd(hmsPart));
                case "d" -> duration = duration.plusDays(getTimeToAdd(hmsPart));
                default -> {
                    // do nothing
                }
            }
        }

        return duration;
    }

    private long getTimeToAdd(@NotNull String hmsPart) {
        return TryUtil.getOr(() -> Long.parseLong(hmsPart.substring(0, hmsPart.length() - 1)),
                NumberFormatException.class,
                e -> 0L);
    }

}
