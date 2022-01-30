package net.qsef1256.diabot.game.paint.model;

import net.qsef1256.diabot.game.paint.data.PaintEntity;

import java.util.List;

public interface PaintManager {

    List<String> getOwnedPaint(long discord_id);

    void delete(long discord_id, String paintName);

    PaintEntity getPaint(long discord_id, String paintName);

    void save(long discord_id, String paintName);

    void overwrite(long discord_id, String paintName);

}
