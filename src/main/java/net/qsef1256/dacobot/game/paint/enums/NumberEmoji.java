package net.qsef1256.dacobot.game.paint.enums;

import lombok.Getter;
import net.qsef1256.dacobot.util.EnumUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public enum NumberEmoji implements Emoji {

    EMPTY("empty", "⬛"),
    ZERO("0", "0️⃣"),
    ONE("1", "1️⃣"),
    TWO("2", "2️⃣"),
    THREE("3", "3️⃣"),
    FOUR("4", "4️⃣"),
    FIVE("5", "5️⃣"),
    SIX("6", "6️⃣"),
    SEVEN("7", "7️⃣"),
    EIGHT("8", "8️⃣"),
    NINE("9", "9️⃣"),
    TEN("10", "\uD83D\uDD1F"),
    RED_ZERO("red0", "<:red0:967331132951625732>"),
    RED_ONE("red1", "<:red1:967331133001981953>"),
    RED_TWO("red2", "<:red2:967331133324947506>"),
    RED_THREE("red3", "<:red3:967331133463359498>"),
    RED_FOUR("red4", "<:red4:967331133278793778>"),
    RED_FIVE("red5", "<:red5:967331133249445938>"),
    RED_SIX("red6", "<:red6:967331133509488690>"),
    RED_SEVEN("red7", "<:red7:967331133266219008>"),
    RED_EIGHT("red8", "<:red8:967331133291364382>"),
    RED_NINE("red9", "<:red9:967331132976816140>"),
    BLACK_ZERO("black0", "<:black0:967331133303959563>"),
    BLACK_ONE("black1", "<:black1:967331133337530438>"),
    BLACK_TWO("black2", "<:black2:967331133266219028>"),
    BLACK_THREE("black3", "<:black3:967331133303947315>"),
    BLACK_FOUR("black4", "<:black4:967331133295575050>"),
    BLACK_FIVE("black5", "<:black5:967331133253627914>"),
    BLACK_SIX("black6", "<:black6:967331133291368469>"),
    BLACK_SEVEN("black7", "<:black7:967331133274615818>"),
    BLACK_EIGHT("black8", "<:black8:967331133253627924>"),
    BLACK_NINE("black9", "<:black9:967331133262032916>");

    private static final Map<String, NumberEmoji> emojiMap = EnumUtil.toMap(NumberEmoji::getEmoji, NumberEmoji.class);
    private static final Map<String, NumberEmoji> idMap = EnumUtil.toMap(NumberEmoji::getId, NumberEmoji.class);

    @Getter
    private final String emoji;
    @Getter
    private final String id;

    NumberEmoji(String id, String emoji) {
        this.id = id;
        this.emoji = emoji;
    }

    @Nullable
    public static NumberEmoji findById(String id) {
        return idMap.get(id);
    }

    @Nullable
    public static NumberEmoji findByEmoji(String emoji) {
        return emojiMap.get(emoji);
    }

}
