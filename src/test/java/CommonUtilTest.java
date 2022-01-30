import net.qsef1256.diabot.util.CommonUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class CommonUtilTest {

    private void testRandom() {
        for (int i = 0; i < 25; i++) {
            System.out.println(CommonUtil.randomInt(-6, -3));
        }
    }

    private void testDiff() {
        assertEquals(2,CommonUtil.getDiff(1,3));
        assertEquals(3,CommonUtil.getDiff(4,1));
        assertEquals(2,CommonUtil.getDiff(-1,-3));
        assertEquals(2,CommonUtil.getDiff(-3,-1));
        assertEquals(6,CommonUtil.getDiff(-3,3));
        assertEquals(3,CommonUtil.getDiff(-3,0));
        assertEquals(0,CommonUtil.getDiff(0,0));
        assertEquals(0,CommonUtil.getDiff(-3,-3));
        assertEquals(0,CommonUtil.getDiff(5,5));
    }

    @Test
    public void testBetween() {
        assertFalse(CommonUtil.isBetween(5, 2, 3));
        assertTrue(CommonUtil.isBetween(5, 2, 5));
        assertTrue(CommonUtil.isBetween(5, 2, 10));
        assertFalse(CommonUtil.isBetween(-5, 2, 3));
        assertFalse(CommonUtil.isBetween(-5, -4, 3));
        assertTrue(CommonUtil.isBetween(-5, -5, 3));
        assertTrue(CommonUtil.isBetween(0, 0, 3));
        assertThrows(IllegalArgumentException.class, () -> CommonUtil.isBetween(-2, -1, -3));
    }

    @Test
    public void testToBetween() {
        assertEquals(1, CommonUtil.toBetween(0,1,3));
        assertThrows(IllegalArgumentException.class, () -> CommonUtil.toBetween(0,4,1));
        assertThrows(IllegalArgumentException.class, () -> CommonUtil.toBetween(3,-1,-3));
        assertEquals(-2,CommonUtil.toBetween(-2,-3,-1));
        assertEquals(2,CommonUtil.toBetween(2,-3,3));
        assertEquals(0,CommonUtil.toBetween(0,-3,0));
        assertEquals(0,CommonUtil.toBetween(0,0,0));
        assertEquals(-3,CommonUtil.toBetween(-1,-3,-3));
        assertEquals(5,CommonUtil.toBetween(10,5,5));
    }
}
