package net.qsef1256.dacobot.util;

import net.qsef1256.dacobot.setting.DiaSetting;
import net.qsef1256.dialib.util.LocalDateTimeUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalDateTimeUtilTest {

    LocalDateTime utcNow = LocalDateTime.now(ZoneId.of("Etc/UTC"));
    LocalDateTime now = ZonedDateTime.now(DiaSetting.getZoneId()).toLocalDateTime();

    @Test
    void isToday() {
        assertTrue(LocalDateTimeUtil.isToday(now));

        assertTrue(LocalDateTimeUtil.isToday(LocalDateTime.now()));
    }

}
