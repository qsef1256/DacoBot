package net.qsef1256.diabot.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * java.util.Date 에 대해서는 Apache Commons 의 DateUtils 를 사용하세요.
 */
@UtilityClass
public class DateUtil {

    /**
     * 첫번째와 두번째 시간이 같은 날짜인지 확인합니다.
     *
     * @param first  first Date
     * @param second second Data
     * @return true if same Day
     */
    public boolean isSameDay(LocalDateTime first, LocalDateTime second) {
        return first.toLocalDate().isEqual(second.toLocalDate());
    }

    /**
     * 해당 시간이 오늘인지 확인합니다.
     *
     * @param time Time to check is today
     * @return true if today
     */
    public boolean isToday(LocalDateTime time) {
        return time.toLocalDate().isEqual(LocalDateTime.now().toLocalDate());
    }

    /**
     * 해당 시간을 `2021-01-24 10:15:30` 꼴로 변환합니다.
     *
     * @param time Time to convert
     * @return ISO LocalDateTime without space
     */
    public String getTimeString(LocalDateTime time) {
        return time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace('T', ' ');
    }

}
