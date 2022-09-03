package net.qsef1256.dacobot.util;

import lombok.experimental.UtilityClass;
import net.qsef1256.dacobot.setting.DiaSetting;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * java.util.Date 에 대해서는 Apache Commons 의 DateUtils 를 사용하세요.
 */
@UtilityClass
public class LocalDateTimeUtil {

    // TODO: Time Zone problem

    private static final ZoneId SYSTEM_ZONE = ZoneId.systemDefault();
    private static final ZoneId ZONE_ID = DiaSetting.getZoneId();

    /**
     * 첫번째와 두번째 시간이 같은 날짜인지 확인합니다.
     *
     * @param first  first Date
     * @param second second Date
     * @return true if same Day
     */
    public boolean isSameDay(@NotNull LocalDateTime first, @NotNull LocalDateTime second) {
        return first.toLocalDate().isEqual(second.toLocalDate());
    }

    /**
     * 해당 시간이 오늘인지 확인합니다.
     *
     * @param time Time to check is today
     * @return true if today
     */
    public boolean isToday(@NotNull LocalDateTime time) {
        return getZonedDateTime(time).toLocalDate().isEqual(getZonedNow().toLocalDate());
    }

    public LocalDateTime getNow() {
        return ZonedDateTime.now(ZONE_ID).toLocalDateTime();
    }

    public ZonedDateTime getZonedNow() {
        return ZonedDateTime.now(ZONE_ID);
    }

    /**
     * LocalDateTime 을 설정에 지정된 ZonedDateTime 으로 변환합니다.
     *
     * @param time fixed time
     * @return zoned date time with setting zone id
     */
    @NotNull
    public ZonedDateTime getZonedDateTime(@NotNull LocalDateTime time) {
        return ZonedDateTime.of(time, SYSTEM_ZONE).withZoneSameInstant(ZONE_ID);
    }

    /**
     * 해당 시간을 `2021-01-24 10:15:30` 꼴로 변환합니다.
     *
     * @param time Time to convert
     * @return ISO LocalDateTime without space
     */
    public String getTimeString(@NotNull LocalDateTime time) {
        return getZonedDateTime(time).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace('T', ' ');
    }

    /**
     * 날짜 차이를 {@link Duration} 으로 반환합니다.
     *
     * @param first  first Date
     * @param second second Date
     * @return difference between first and second
     */
    public Duration getDiff(@NotNull LocalDateTime first, @NotNull LocalDateTime second) {
        return Duration.between(first, second);
    }

}
