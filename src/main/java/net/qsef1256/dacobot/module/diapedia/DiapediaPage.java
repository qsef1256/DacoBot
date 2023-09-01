package net.qsef1256.dacobot.module.diapedia;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.qsef1256.dacobot.setting.constants.DiaImage;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiapediaPage {

    private final String id;
    private final Map<String, Object> values = new HashMap<>();
    private final List<String> categories = new ArrayList<>();

    public DiapediaPage(@NotNull String id, @NotNull Map<String, Object> map) {
        this.id = id;

        map.forEach((key, o) -> {
            if (o instanceof Map) {
                categories.add(key);
            } else {
                values.put(key, o);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public MessageEmbed toEmbed() {
        EmbedBuilder embed = DiaEmbed.primary(id, null, null);
        embed.setAuthor("Diapedia", null, DiaImage.MAIN_THUMBNAIL);

        if (!categories.isEmpty())
            embed.addField("하위 카테고리", String.join(", ", categories), false);
        values.forEach((key, o) -> {
            if (o instanceof List)
                embed.addField(key, String.join(", ", (List<String>) o), false);
            else
                embed.addField(key, o.toString(), false);
        });

        return embed.build();
    }

    @Override
    public String toString() {
        return "id: " + id +
                " categories: " + String.join(",", categories) +
                " values: " + values;
    }

}
