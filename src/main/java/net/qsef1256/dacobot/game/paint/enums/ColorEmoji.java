package net.qsef1256.dacobot.game.paint.enums;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private static final Map<String, ColorEmoji> emojiMap =
            Collections.unmodifiableMap(Stream.of(values()).collect(Collectors.toMap(ColorEmoji::getEmoji, Function.identity())));
    private static final Map<String, ColorEmoji> idMap =
            Collections.unmodifiableMap(Stream.of(values()).collect(Collectors.toMap(ColorEmoji::getId, Function.identity())));
    private static final Map<Color, ColorEmoji> colorMap =
            Collections.unmodifiableMap(Stream.of(values()).collect(Collectors.toMap(ColorEmoji::getColor, Function.identity())));
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
