package net.qsef1256.dacobot.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonUtilTest {

    @Test
    void testDiff() {
        assertEquals(2, CommonUtil.getDiff(1, 3));
        assertEquals(3, CommonUtil.getDiff(4, 1));
        assertEquals(2, CommonUtil.getDiff(-1, -3));
        assertEquals(2, CommonUtil.getDiff(-3, -1));
        assertEquals(6, CommonUtil.getDiff(-3, 3));
        assertEquals(3, CommonUtil.getDiff(-3, 0));
        assertEquals(0, CommonUtil.getDiff(0, 0));
        assertEquals(0, CommonUtil.getDiff(-3, -3));
        assertEquals(0, CommonUtil.getDiff(5, 5));
    }

    @Test
    void testBetween() {
        assertFalse(CommonUtil.isBetween(2, 5, 3));
        assertTrue(CommonUtil.isBetween(2, 5, 5));
        assertTrue(CommonUtil.isBetween(2, 5, 10));
        assertFalse(CommonUtil.isBetween(2, -5, 3));
        assertFalse(CommonUtil.isBetween(-4, -5, 3));
        assertTrue(CommonUtil.isBetween(-5, -5, 3));
        assertTrue(CommonUtil.isBetween(0, 0, 3));
        assertThrows(IllegalArgumentException.class, () -> CommonUtil.isBetween(-1, -2, -3));
    }

    @Test
    void testToBetween() {
        assertEquals(1, CommonUtil.toBetween(1, 0, 3));
        assertThrows(IllegalArgumentException.class, () -> CommonUtil.toBetween(4, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> CommonUtil.toBetween(-1, 3, -3));
        assertEquals(-2, CommonUtil.toBetween(-3, -2, -1));
        assertEquals(2, CommonUtil.toBetween(-3, 2, 3));
        assertEquals(0, CommonUtil.toBetween(-3, 0, 0));
        assertEquals(0, CommonUtil.toBetween(0, 0, 0));
        assertEquals(-3, CommonUtil.toBetween(-3, -1, -3));
        assertEquals(5, CommonUtil.toBetween(5, 10, 5));
    }

    @Test
    void testMatch() {
        String a = "a";
        String b = "b";

        assertTrue(CommonUtil.allSame(a, a, a, a));
        assertFalse(CommonUtil.allSame(a, b, a, a));
        assertFalse(CommonUtil.allSame("a", "a", "a", "b"));
        assertTrue(CommonUtil.allSame(null, null, null, null));
        assertFalse(CommonUtil.allSame(null, null, "a", null));
        assertTrue(CommonUtil.anySame(a, a, a, a));
        assertTrue(CommonUtil.anySame(a, b, b, a));
        assertFalse(CommonUtil.anySame("b", "c", "d", "e"));
        assertTrue(CommonUtil.anySame("b", "c", "c", "e"));
        assertTrue(CommonUtil.anySame(null, null, "a", "b"));
        assertTrue(CommonUtil.allSame(true));

        assertTrue(CommonUtil.anyNull(null, "a", "a", "a"));
        assertFalse(CommonUtil.anyNull("a", "a", "a", "a"));
        assertTrue(CommonUtil.anyNull(null, null, null, null));
        assertTrue(CommonUtil.anyNull("a", "b", "c", null));
    }

}
