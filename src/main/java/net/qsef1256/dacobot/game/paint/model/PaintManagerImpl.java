package net.qsef1256.dacobot.game.paint.model;

import net.qsef1256.dacobot.DacoBot;
import net.qsef1256.dacobot.database.DaoCommon;
import net.qsef1256.dacobot.database.DaoCommonImpl;
import net.qsef1256.dacobot.game.paint.data.PaintEntity;
import net.qsef1256.dacobot.game.paint.model.painter.Painter;
import net.qsef1256.dacobot.game.paint.model.painter.PainterContainer;
import net.qsef1256.dacobot.util.DiscordUtil;
import org.jetbrains.annotations.NotNull;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaintManagerImpl implements PaintManager {

    private static final DaoCommon<PaintEntity, String> dao = new DaoCommonImpl<>(PaintEntity.class);

    @Override
    public List<String> getOwnedPaint(long discord_id) {
        List<String> paintNames = new ArrayList<>();

        Map<String, Object> constraint = new HashMap<>();
        constraint.put("ownerId", discord_id);
        List<PaintEntity> paints = dao.findBy(constraint);
        for (PaintEntity paintData : paints) {
            paintNames.add(paintData.getPaintName());
        }
        return paintNames;
    }

    @Override
    public void delete(long discord_id, String paintName) {
        findPaint(paintName, discord_id);

        dao.deleteById(paintName);
    }

    @NotNull
    protected PaintEntity findPaint(String paintName, long discord_id) {
        PaintEntity paintData;
        if (!dao.existsById(paintName)) throw new IllegalArgumentException(paintName + " 이름의 그림은 없습니다.");

        paintData = dao.findById(paintName);
        checkPermission(discord_id, paintData.getOwnerId());

        return paintData;
    }

    private void checkPermission(long discord_id, Long ownerId) {
        if (ownerId != discord_id && DacoBot.getCommandClient().getOwnerIdLong() != discord_id)
            throw new IllegalArgumentException("그림을 편집할 권한이 없습니다. 소유자: " + DiscordUtil.getNameAsTag(ownerId));
    }

    @Override
    public PaintEntity getPaint(long discord_id, String paintName) {
        return findPaint(paintName, discord_id);
    }

    @Override
    public void save(long discord_id, String paintName) {
        PaintEntity paintData = new PaintEntity();

        if (dao.existsById(paintName))
            throw new KeyAlreadyExistsException(paintName + " 이름의 그림은 이미 있습니다.");
        Painter painter = PainterContainer.getPainter(discord_id);

        try {
            setPaintData(paintData, paintName, painter, discord_id);

            dao.save(paintData);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void overwrite(long discord_id, String paintName) {
        PaintEntity paintData = new PaintEntity();
        Painter painter = PainterContainer.getPainter(discord_id);

        try {
            setPaintData(paintData, paintName, painter, discord_id);

            dao.save(paintData);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void setPaintData(@NotNull PaintEntity paintData, String paintName, @NotNull Painter painter, long discord_id) {
        paintData
                .setPaintName(paintName)
                .setXSize(painter.getWidth())
                .setYSize(painter.getHeight())
                .setPixels(painter.getPixels())
                .setCreatedUserId(discord_id)
                .setOwnerId(discord_id);
    }
}
