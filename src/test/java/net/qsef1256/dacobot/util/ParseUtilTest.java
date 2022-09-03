package net.qsef1256.dacobot.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ParseUtilTest {

    @Test
    void isInteger() {
        int a = 1;

        assertTrue(ParseUtil.canInteger(a));
    }

}