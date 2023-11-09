package net.qsef1256.dacobot.localization;

import net.qsef1256.dacobot.core.localization.TimeLocalizer;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TimeLocalizerTest {

    @Test
    void testFormat() {
        assertDoesNotThrow(() -> { // TODO: assertEquals
            System.out.println(TimeLocalizer.format(Duration.ofHours(1)));

            System.out.println(TimeLocalizer.format(Duration.ofDays(370)
                    .plusHours(3)
                    .plusMinutes(23)
                    .plusSeconds(35)));
        });
    }

}
