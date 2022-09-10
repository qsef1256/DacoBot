package net.qsef1256.dacobot.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RandomUtilTest {

    @Test
    void testRandom() {
        int lowest = -6;
        int highest = 1;

        for (int i = 0; i < 25; i++) {
            int random = RandomUtil.randomInt(lowest, highest);
            assertTrue(lowest <= random && random <= highest);
        }
    }

    @Test
    void testRandomPick() {
        List<Integer> test = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> smallTest = List.of(1, 2);
        List<Integer> empty = List.of();
        int count = 3;

        assertDoesNotThrow(() -> RandomUtil.getRandomElement(test));
        assertNull(RandomUtil.getRandomElement(empty));
        assertTrue(() -> RandomUtil.getMultiRandomElement(test, count).size() == count);
        assertEquals(RandomUtil.getMultiRandomElement(empty, count), List.of());
        assertEquals(RandomUtil.getMultiRandomElement(empty, 1), List.of());
        assertEquals(RandomUtil.getMultiRandomElement(test, 0), List.of());
        assertEquals(RandomUtil.getMultiRandomElement(test, -1), List.of());
        assertEquals(RandomUtil.getMultiRandomElement(smallTest, 5).size(), smallTest.size());
    }

}
