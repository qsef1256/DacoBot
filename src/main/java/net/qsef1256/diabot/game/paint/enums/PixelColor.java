package net.qsef1256.diabot.game.paint.enums;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum PixelColor {

    RED('r', "🟥", Color.RED),
    ORANGE('o', "🟧", Color.ORANGE),
    YELLOW('y', "🟨", Color.YELLOW),
    GREEN('g', "🟩", Color.GREEN),
    BLUE('b', "🟦", Color.BLUE),
    PURPLE('p', "🟪", Color.MAGENTA),
    BROWN('n', "🟫", new Color(107, 52, 0)),
    BLACK('k', "⬛", Color.BLACK),
    WHITE('w', "⬜", Color.WHITE);
    /*ZERO('0',"0️⃣"),
    ONE('1',"1️⃣"),
    TWO('2',"2️⃣"),
    THREE('3',"3️⃣"),
    FOUR('4',"4️⃣"),
    FIVE('5',"5️⃣"),
    SIX('6',"6️⃣"),
    SEVEN('7',"7️⃣"),
    EIGHT('8',"8️⃣"),
    NINE('9',"9️⃣"),
    UP('w',"⬆"),
    DOWN('s',"⬇"),
    LEFT('a',"⬅"),
    RIGHT('d',"➡"),
    HASH('#',"#️⃣"),
    PLUS('+',"➕"),
    MINUS('-',"➖"),
    MULTI('*',"✖"),
    DIV('/',"➗"),
    HEART('t',"❤"),
    QUESTION('?',"❓"),
    EXCLAMATION('!',"❗"),;*/

    private static final Map<String, PixelColor> emojiMap =
            Collections.unmodifiableMap(Stream.of(values()).collect(Collectors.toMap(PixelColor::getEmoji, Function.identity())));
    private static final Map<Character, PixelColor> idMap =
            Collections.unmodifiableMap(Stream.of(values()).collect(Collectors.toMap(PixelColor::getId, Function.identity())));
    private static final Map<Color, PixelColor> colorMap =
            Collections.unmodifiableMap(Stream.of(values()).collect(Collectors.toMap(PixelColor::getColor, Function.identity())));
    @Getter
    private final String emoji;
    @Getter
    private final char id;
    @Getter
    private Color color;

    PixelColor(char id, String emoji) {
        this.id = id;
        this.emoji = emoji;
        color = null;
    }

    PixelColor(char id, String emoji, Color color) {
        this(id, emoji);
        this.color = color;
    }

    @Nullable
    public static PixelColor findByEmoji(String emojiValue) {
        return emojiMap.get(emojiValue);
    }

    @Nullable
    public static PixelColor findById(char idValue) {
        return idMap.get(idValue);
    }

    @Nullable
    public static PixelColor findByColor(Color colorValue) {
        return colorMap.get(colorValue);
    }

}
