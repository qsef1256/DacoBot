package net.qsef1256.dacobot.localization;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TimeLocalizerTest {

    @Test
    void testFormat() {
        assertDoesNotThrow(() -> { // TODO: assertEquals
            System.out.println(TimeLocalizer.format(Duration.ofHours(1)));

            System.out.println(TimeLocalizer.format(Duration.ofDays(370).plusHours(3).plusMinutes(23).plusSeconds(35)));
        });
    }

}

// FIXME: Gitlab TOKEN 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
// Use and expire
// whatever...