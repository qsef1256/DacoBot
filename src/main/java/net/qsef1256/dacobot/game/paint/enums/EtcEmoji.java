package net.qsef1256.dacobot.game.paint.enums;

import lombok.Getter;
import net.qsef1256.dialib.util.EnumUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public enum EtcEmoji implements Emoji {

    UP("w", "⬆"),
    DOWN("s", "⬇"),
    LEFT("a", "⬅"),
    RIGHT("d", "➡"),
    HASH("#", "#️⃣"),
    PLUS("+", "➕"),
    MINUS("-", "➖"),
    MULTI("*", "✖"),
    DIV("/", "➗"),
    HEART("t", "❤"),
    QUESTION("?", "❓"),
    EXCLAMATION("!", "❗");

    private static final Map<String, EtcEmoji> emojiMap = EnumUtil.toMap(EtcEmoji::getEmoji, EtcEmoji.class);
    private static final Map<String, EtcEmoji> idMap = EnumUtil.toMap(EtcEmoji::getId, EtcEmoji.class);

    @Getter
    private final String emoji;
    @Getter
    private final String id;

    EtcEmoji(@NotNull String id, @NotNull String emoji) {
        if (id.length() != 1) throw new IllegalArgumentException("id can't longer than 1 character: " + id);

        this.id = id;
        this.emoji = emoji;
    }

    @Nullable
    public static EtcEmoji findByEmoji(@NotNull String emoji) {
        return emojiMap.get(emoji);
    }

    @Nullable
    public static EtcEmoji findById(String id) {
        return idMap.get(id);
    }

}
