package net.qsef1256.dacobot.localization;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

// TODO: English and use Locale class

@UtilityClass
public class TimeLocalizer {

    public final String DAY = "일";
    public final String HOUR = "시";
    public final String MINUTE = "분";
    public final String SECOND = "초";
    public final String MILLI_SECOND = "밀리초";

    public String format(@NotNull Duration duration) {
        StringBuilder sb = new StringBuilder();

        if (duration.toDaysPart() != 0) sb.append(" ").append(duration.toDaysPart()).append(DAY);
        if (duration.toHoursPart() != 0) sb.append(" ").append(duration.toHoursPart()).append(HOUR);
        if (duration.toMinutesPart() != 0) sb.append(" ").append(duration.toMinutesPart()).append(MINUTE);
        if (duration.toSecondsPart() != 0) sb.append(" ").append(duration.toSecondsPart()).append(SECOND);
        if (duration.toMillisPart() != 0) sb.append(" ").append(duration.toMillisPart()).append(MILLI_SECOND);

        return sb.toString().trim();
    }

}
