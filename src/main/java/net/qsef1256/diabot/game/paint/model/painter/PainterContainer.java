package net.qsef1256.diabot.game.paint.model.painter;

import java.util.HashMap;
import java.util.Map;

public class PainterContainer {

    private static final Map<Long, Painter> userPainter = new HashMap<>();

    public static Painter getPainter(long discord_id) {
        if (!userPainter.containsKey(discord_id)) {
            userPainter.put(discord_id, new Painter());
        }
        return userPainter.get(discord_id);
    }

}
