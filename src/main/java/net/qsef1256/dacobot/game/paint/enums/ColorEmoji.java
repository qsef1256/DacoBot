package net.qsef1256.dacobot.game.paint.enums;

import lombok.Getter;
import net.qsef1256.dacobot.util.EnumUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Map;

public enum ColorEmoji implements Emoji {

    RED("r", "🟥", Color.RED),
    ORANGE("o", "🟧", Color.ORANGE),
    YELLOW("y", "🟨", Color.YELLOW),
    GREEN("g", "🟩", Color.GREEN),
    BLUE("b", "🟦", Color.BLUE),
    PURPLE("p", "🟪", Color.MAGENTA),
    BROWN("n", "🟫", new Color(107, 52, 0)),
    BLACK("k", "⬛", Color.BLACK),
    WHITE("w", "⬜", Color.WHITE);

    private static final Map<String, ColorEmoji> emojiMap = EnumUtil.toMap(ColorEmoji::getEmoji, ColorEmoji.class);
    private static final Map<String, ColorEmoji> idMap = EnumUtil.toMap(ColorEmoji::getId, ColorEmoji.class);
    private static final Map<Color, ColorEmoji> colorMap = EnumUtil.toMap(ColorEmoji::getColor, ColorEmoji.class);

    @Getter
    private final String emoji;
    @Getter
    private final String id;
    @Getter
    private Color color;

    ColorEmoji(@NotNull String id, @NotNull String emoji) {
        if (id.length() != 1) throw new IllegalArgumentException("id can't longer than 1 character: " + id);

        this.id = id;
        this.emoji = emoji;
        color = null;
    }

    ColorEmoji(@NotNull String id, @NotNull String emoji, @NotNull Color color) {
        this(id, emoji);
        this.color = color;
    }

    @Nullable
    public static ColorEmoji findByEmoji(@NotNull String emoji) {
        return emojiMap.get(emoji);
    }

    @Nullable
    public static ColorEmoji findById(String id) {
        return idMap.get(id);
    }

    @Nullable
    public static ColorEmoji findByColor(Color color) {
        return colorMap.get(color);
    }

}
