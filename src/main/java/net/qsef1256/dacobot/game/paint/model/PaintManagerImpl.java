package net.qsef1256.dacobot.game.paint.model;

import net.qsef1256.dacobot.DacoBot;
import net.qsef1256.dacobot.database.DaoCommon;
import net.qsef1256.dacobot.database.DaoCommonHibernateImpl;
import net.qsef1256.dacobot.game.paint.data.PaintEntity;
import net.qsef1256.dacobot.game.paint.model.painter.PainterContainer;
import net.qsef1256.dacobot.game.paint.model.painter.PixelPainter;
import net.qsef1256.dacobot.util.JDAUtil;
import org.jetbrains.annotations.NotNull;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaintManagerImpl implements PaintManager {

    private static final DaoCommon<PaintEntity, String> dao = new DaoCommonHibernateImpl<>(PaintEntity.class);

    @Override
    public List<String> getOwnedPaint(long discordId) {
        List<String> paintNames = new ArrayList<>();

        Map<String, Object> constraint = new HashMap<>();
        constraint.put("ownerId", discordId);
        List<PaintEntity> paints = dao.findBy(constraint);
        for (PaintEntity paintData : paints) {
            paintNames.add(paintData.getPaintName());
        }
        return paintNames;
    }

    @Override
    public void delete(long discordId, String paintName) {
        findPaint(paintName, discordId);

        dao.deleteById(paintName);
    }

    @NotNull
    protected PaintEntity findPaint(String paintName, long discordId) {
        PaintEntity paintData;
        if (!dao.existsById(paintName)) throw new IllegalArgumentException(paintName + " 이름의 그림은 없습니다.");

        paintData = dao.findById(paintName);
        checkPermission(discordId, paintData.getOwnerId());

        return paintData;
    }

    private void checkPermission(long discordId, Long ownerId) {
        if (ownerId != discordId && DacoBot.getCommandClient().getOwnerIdLong() != discordId)
            throw new IllegalArgumentException("그림을 편집할 권한이 없습니다. 소유자: " + JDAUtil.getNameAsTag(ownerId));
    }

    @Override
    public PaintEntity getPaint(long discordId, String paintName) {
        return findPaint(paintName, discordId);
    }

    @Override
    public void save(long discordId, String paintName) {
        PaintEntity paintData = new PaintEntity();

        if (dao.existsById(paintName))
            throw new KeyAlreadyExistsException(paintName + " 이름의 그림은 이미 있습니다.");
        PixelPainter painter = (PixelPainter) PainterContainer.getPainter(discordId);

        try {
            setPaintData(paintData, paintName, painter, discordId);

            dao.save(paintData);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void overwrite(long discordId, String paintName) {
        PaintEntity paintData = new PaintEntity();
        PixelPainter painter = (PixelPainter) PainterContainer.getPainter(discordId);

        try {
            setPaintData(paintData, paintName, painter, discordId);

            dao.save(paintData);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void setPaintData(@NotNull PaintEntity paintData, String paintName, @NotNull PixelPainter painter, long discordId) {
        paintData
                .setPaintName(paintName)
                .setXSize(painter.getWidth())
                .setYSize(painter.getHeight())
                .setPixels(painter.getPixelEntities())
                .setCreatedUserId(discordId)
                .setOwnerId(discordId);
    }
}
