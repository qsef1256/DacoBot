package net.qsef1256.dacobot.command;

import net.qsef1256.dialib.util.GenericUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class HelpConfigTest {

    public static Map<String, Map<?, ?>> categories = new HashMap<>();

    private static void printMapRecursive(final @NotNull Map<?, ?> data) {
        for (Object key : data.keySet()) {
            final Object value = data.get(key);
            if (value instanceof Map) {
                System.out.println("MAP: value for " + key + " is a Map - recursing");
                printMapRecursive((Map<?, ?>) value);
            } else {
                System.out.println("VAL: value " + value + " for " + key + " is type " + value.getClass());
            }
        }
    }

    @Test
    void testConfigFile() {
        assertDoesNotThrow(() -> {
            Yaml yaml = new Yaml();
            try (InputStream in = getClass().getClassLoader().getResourceAsStream("commands.yml")) {
                // printMapRecursive(yaml.load(in));
                Map<String, Object> categories = yaml.load(in);

                displayHelp(categories);
            }

            categories.forEach((key, category) -> {
                System.out.println(key);
                System.out.println(category);
            });
        });
    }

    // ??
    private void displayHelp(@NotNull Map<?, ?> map) {
        if (map.containsKey("CATEGORIES")) {
            Map<?, ?> category = (Map<?, ?>) map.get("CATEGORIES");
            category.forEach((key, value) -> {
                if (value instanceof Map<?, ?> values) {
                    String title = Optional.ofNullable(values.get("TITLE").toString()).orElse("제목 없음");
                    String desc = Optional.ofNullable(values.get("DESC").toString()).orElse("제목 없음");
                    ArrayList<?> list = GenericUtil.getArrayList(values.get("LIST"));
                    System.out.println(title + "(" + desc + ") " + list);

                    if (values.get("CATEGORIES") instanceof Map<?, ?> subMap) {
                        subMap.forEach((subKey, subValue) -> {
                            if (subValue instanceof Map<?, ?> subValues) {
                                String subTitle = Optional.ofNullable(subValues.get("TITLE").toString()).orElse("제목 없음");
                                String subDesc = Optional.ofNullable(subValues.get("DESC").toString()).orElse("제목 없음");
                                System.out.println("Sub: " + subTitle + "(" + subDesc + ")");
                            }
                        });
                    }
                }
            });
        }
    }

    @Deprecated
    private void displayHelp(@NotNull Map<?, ?> map, String a) {
        if (map.containsKey("CATEGORIES")) {
            Map<?, ?> category = (Map<?, ?>) map.get("CATEGORIES");
            category.forEach((key, valueMap) -> {
                if (valueMap instanceof Map<?, ?> values) {
                    String title = Optional.ofNullable(values.get("TITLE").toString()).orElse("제목 없음");
                    String desc = Optional.ofNullable(values.get("DESC").toString()).orElse("제목 없음");
                    ArrayList<?> list = GenericUtil.getArrayList(values.get("LIST"));
                    System.out.println("Putting category: " + title);
                    categories.put(title, values);

                    if (values.get("CATEGORIES") instanceof Map<?, ?>) {
                        System.out.println("recursive");
                        displayHelp(values);
                    }
                }
            });
        }
    }

}
