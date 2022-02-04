import net.qsef1256.diabot.game.paint.model.painter.Painter;
import net.qsef1256.diabot.struct.NestedMap;
import net.qsef1256.diabot.util.CommonUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommonUtilTest {

    private void testRandom() {
        for (int i = 0; i < 25; i++) {
            System.out.println(CommonUtil.randomInt(-6, -3));
        }
    }

    private void testDiff() {
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
        assertEquals(1, CommonUtil.toBetween(0, 1, 3));
        assertThrows(IllegalArgumentException.class, () -> CommonUtil.toBetween(0, 4, 1));
        assertThrows(IllegalArgumentException.class, () -> CommonUtil.toBetween(3, -1, -3));
        assertEquals(-2, CommonUtil.toBetween(-2, -3, -1));
        assertEquals(2, CommonUtil.toBetween(2, -3, 3));
        assertEquals(0, CommonUtil.toBetween(0, -3, 0));
        assertEquals(0, CommonUtil.toBetween(0, 0, 0));
        assertEquals(-3, CommonUtil.toBetween(-1, -3, -3));
        assertEquals(5, CommonUtil.toBetween(10, 5, 5));
    }

    @Test
    public void testMatch() {
        String a = "a";

        assertTrue(CommonUtil.allSame(a, a, a, a));
        assertFalse(CommonUtil.allSame("a", "a", "a", "b"));
        assertTrue(CommonUtil.allSame(null, null, null, null));
        assertFalse(CommonUtil.allSame(null, null, "a", null));
        assertTrue(CommonUtil.anySame(a, a, a, a));
        assertFalse(CommonUtil.anySame("b", "c", "d", "e"));
        assertTrue(CommonUtil.anySame("b", "c", "c", "e"));
        assertTrue(CommonUtil.anySame(null, null, "a", "b"));
        assertTrue(CommonUtil.allSame(true));

        assertTrue(CommonUtil.anyNull(null, "a", "a", "a"));
        assertFalse(CommonUtil.anyNull("a", "a", "a", "a"));
        assertTrue(CommonUtil.anyNull(null, null, null, null));
        assertTrue(CommonUtil.anyNull("a", "b", "c", null));
    }

    @Test
    public void testNestedMap() {
        NestedMap<String, String> table = new NestedMap<>();

        table.put("c", "user", "c");
        table.put("c", "asdf", "c");
        table.put("a", "painter", new Painter());
        table.put("a", "user", "a");
        table.put("a", "asdf", "a");
        table.put("b", "asdf", "b");
        table.put("b", "user", "b");

        Painter painter = table.get("a", "painter");
        if (painter != null) {
            System.out.println(painter.printToString());
        }
        System.out.println(table.get("a", "asdf") + "");
        System.out.println(table.keySet("a"));
        table.forEach((mainKey, valueMap) ->
                valueMap.forEach((subKey, value) -> System.out.println(mainKey + " " + subKey + " " + value)));
    }

}
