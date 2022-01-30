package net.qsef1256.diabot.enums;

import lombok.Getter;

public enum NumberEmoji {

    ZERO("0️⃣"),
    ONE("1️⃣"),
    TWO("2️⃣"),
    THREE("3️⃣"),
    FOUR("4️⃣"),
    FIVE("5️⃣"),
    SIX("6️⃣"),
    SEVEN("7️⃣"),
    EIGHT("8️⃣"),
    NINE("9️⃣"),
    TEN("\uD83D\uDD1F");

    private static final NumberEmoji[] list = NumberEmoji.values();
    @Getter
    final String emoji;

    NumberEmoji(String emoji) {
        this.emoji = emoji;
    }

    public static String get(int i) {
        return list[i].getEmoji();
    }

    public static int size() {
        return list.length - 1;
    }

}
