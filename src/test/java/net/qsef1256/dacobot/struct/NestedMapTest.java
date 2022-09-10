package net.qsef1256.dacobot.struct;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class NestedMapTest {

    @Test
    void testNestedMap() {
        NestedMap<String, String> table = new NestedMap<>();
        NestedMap<String, String> sameTable = new NestedMap<>();
        NestedMap<String, String> diffTable = new NestedMap<>();

        putData(table);
        putData(sameTable);
        putData(diffTable);
        diffTable.put("d", "test", "c");

        System.out.println(table.get("a", "asdf") + "");
        System.out.println(table.keySet("a"));
        table.forEach((mainKey, valueMap) ->
                valueMap.forEach((subKey, value) -> System.out.println(mainKey + " " + subKey + " " + value)));

        System.out.printf("hashCodes: %s %s %s\n", table.hashCode(), sameTable.hashCode(), diffTable.hashCode());
        System.out.println("sameTable equals: " + table.equals(sameTable));
        System.out.println("diffTable equals: " + table.equals(diffTable));

        table.remove("c", "asdf");
        assertFalse(table.contains("c", "asdf"));

        table.replace("asd", "test", "aa");
        assertFalse(table.contains("asd", "test"));
    }

    private void putData(@NotNull NestedMap<String, String> table) {
        table.put("c", "user", "c");
        table.put("c", "asdf", "c");
        table.put("a", "user", "a");
        table.put("a", "asdf", "a");
        table.put("b", "asdf", "b");
        table.put("b", "user", "b");
    }

}
