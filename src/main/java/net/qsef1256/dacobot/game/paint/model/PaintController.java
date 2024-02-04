package net.qsef1256.dacobot.game.paint.model;

import net.qsef1256.dacobot.game.paint.entity.PaintEntity;

import java.util.List;

public interface PaintController {

    List<String> getOwnedPaint(long discordId);

    void delete(long discordId, String paintName);

    PaintEntity getPaint(long discordId, String paintName);

    void save(long discordId, String paintName);

    void overwrite(long discordId, String paintName);

}
