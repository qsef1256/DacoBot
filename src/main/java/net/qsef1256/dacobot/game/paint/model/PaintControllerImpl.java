package net.qsef1256.dacobot.game.paint.model;

import com.jagrosh.jdautilities.command.CommandClient;
import net.qsef1256.dacobot.game.paint.entity.PaintEntity;
import net.qsef1256.dacobot.game.paint.model.painter.PainterContainer;
import net.qsef1256.dacobot.game.paint.model.painter.PixelPainter;
import net.qsef1256.dacobot.game.paint.repository.PaintRepository;
import net.qsef1256.dacobot.util.JDAService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PaintControllerImpl implements PaintController {

    private final JDAService jdaService;
    private final CommandClient commandClient;
    private final PaintRepository paintRepository;

    public PaintControllerImpl(@NotNull JDAService jdaService,
                               @NotNull CommandClient commandClient,
                               @NotNull PaintRepository paintRepository) {
        this.jdaService = jdaService;
        this.commandClient = commandClient;
        this.paintRepository = paintRepository;
    }

    @Override
    public List<String> getOwnedPaint(long discordId) {
        List<String> paintNames = new ArrayList<>();
        List<PaintEntity> paints = paintRepository.findByOwnerId(discordId);
        for (PaintEntity paintData : paints) {
            paintNames.add(paintData.getPaintName());
        }

        return paintNames;
    }

    @Override
    public void delete(long discordId, String paintName) {
        findPaint(paintName, discordId);

        paintRepository.deleteById(paintName);
    }

    @NotNull
    protected PaintEntity findPaint(String paintName, long discordId) {
        PaintEntity paintData = paintRepository
                .findById(paintName)
                .orElseThrow(() -> new IllegalArgumentException(paintName + " 이름의 그림은 없습니다."));
        checkPermission(discordId, paintData.getOwnerId());

        return paintData;
    }

    private void checkPermission(long discordId, long ownerId) {
        if (ownerId != discordId && commandClient.getOwnerIdLong() != discordId)
            throw new IllegalArgumentException("그림을 편집할 권한이 없습니다. 소유자: " + jdaService.getNameAsTag(ownerId));
    }

    @Override
    public PaintEntity getPaint(long discordId, String paintName) {
        return findPaint(paintName, discordId);
    }

    @Override
    public void save(long discordId, String paintName) {
        if (paintRepository.existsById(paintName))
            throw new KeyAlreadyExistsException(paintName + " 이름의 그림은 이미 있습니다.");

        paintRepository.saveAndFlush(getPaintEntity(discordId, paintName));
    }

    @Override
    public void overwrite(long discordId, String paintName) {
        paintRepository.saveAndFlush(getPaintEntity(discordId, paintName));
    }

    @NotNull
    private PaintEntity getPaintEntity(long discordId, String paintName) {
        PaintEntity paintData = new PaintEntity();
        PixelPainter painter = (PixelPainter) PainterContainer.getPainter(discordId);
        paintData.setPaintName(paintName)
                .setXSize(painter.getWidth())
                .setYSize(painter.getHeight())
                .setPixels(painter.getPixelEntities())
                .setCreatedUserId(discordId)
                .setOwnerId(discordId);

        return paintData;
    }

}
