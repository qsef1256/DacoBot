package net.qsef1256.dacobot.service.diapedia;

import lombok.Getter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dialib.io.FileLoader;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.util.Map;
import java.util.TreeMap;

public class Diapedia {

    @Getter
    private static final Diapedia instance = new Diapedia();

    private DiapediaPage index;
    private final Map<String, DiapediaPage> pages = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private Diapedia() {
        init();
    }

    @SneakyThrows
    public void init() {
        Map<String, Object> map = new Yaml().load(new FileReader(FileLoader.get(
                "diapedia.yml",
                Diapedia.class)));

        load(map);
        loadIndex(map);
    }

    @SuppressWarnings("unchecked")
    public void load(@NotNull Map<String, Object> map) {
        map.forEach((s, o) -> {
            if (o instanceof Map) {
                Map<String, Object> data = (Map<String, Object>) o;
                pages.put(s, new DiapediaPage(s, data));

                load(data);
            }
        });
    }

    public void loadIndex(@NotNull Map<String, Object> map) {
        index = new DiapediaPage("목차", map);
    }

    public MessageEmbed index() {
        return index.toEmbed();
    }

    public MessageEmbed search(String name) {
        if (pages.containsKey(name)) {
            return pages.get(name).toEmbed();
        } else {
            return DiaEmbed.error(name, "해당 검색어를 찾지 못했습니다.", null, null).build();
        }
    }

}
