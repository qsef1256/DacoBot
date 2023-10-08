package net.qsef1256.dacobot.util;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeUtilTest {

    @Test
    void parseHms() {
        assertEquals(
                Duration.ofSeconds(30 + 24 * 60 + 10 * 3600),
                TimeUtil.parseHms("10h 24m 30s"));

        assertEquals(Duration.ZERO, TimeUtil.parseHms("invalid text"));
        assertEquals(Duration.ZERO, TimeUtil.parseHms("ah bm css"));
    }

}