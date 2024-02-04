package net.qsef1256.dacobot.util;

import net.qsef1256.dacobot.setting.DiaSetting;
import net.qsef1256.dialib.util.LocalDateTimeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class LocalDateTimeUtilTest {

    private LocalDateTime utcNow;
    private LocalDateTime now;

    LocalDateTimeUtilTest(@Autowired DiaSetting diaSetting) {
        utcNow = LocalDateTime.now(ZoneId.of("Etc/UTC"));
        now = ZonedDateTime.now(diaSetting.getZoneId()).toLocalDateTime();
    }

    @Test
    void isToday() {
        assertTrue(LocalDateTimeUtil.isToday(now));
        assertTrue(LocalDateTimeUtil.isToday(LocalDateTime.now()));
    }

}
