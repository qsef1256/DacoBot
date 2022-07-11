package net.qsef1256.dacobot.game.paint.model.painter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PainterContainer {

    private static final Map<Long, Painter> userPainter = new HashMap<>();

    public static Painter getPainter(long discordId) {
        return userPainter.computeIfAbsent(discordId, id -> new PixelPainter());
    }

}
